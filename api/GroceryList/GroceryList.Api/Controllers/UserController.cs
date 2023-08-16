﻿using GroceryList.Api.Extensions;
using GroceryList.Api.Models;
using GroceryList.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.WebUtilities;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace GroceryList.Api.Controllers;

[Route("api/[controller]")]
[ApiController]
public class UserController : ControllerBase
{
    private readonly UserManager<IdentityUser> _userManager;
    private readonly ICartService _cartService;
    private readonly IUserCartService _userCartService;
    private readonly IItemService _itemService;
    private readonly IUserService _userService;
    private readonly JwtOptions _jwtOptions;

    public UserController(UserManager<IdentityUser> userManager,
                          IOptions<JwtOptions> jwtOptions,
                          ICartService cartService,
                          IUserCartService userCartService,
                          IItemService itemService,
                          IUserService userService)
    {
        _userManager = userManager ?? throw new ArgumentNullException(nameof(userManager));
        _cartService = cartService ?? throw new ArgumentNullException(nameof(cartService));
        _userCartService = userCartService ?? throw new ArgumentNullException(nameof(userCartService));
        _itemService = itemService ?? throw new ArgumentNullException(nameof(itemService));
        _userService = userService ?? throw new ArgumentNullException(nameof(userService));
        _jwtOptions = jwtOptions.Value ?? throw new ArgumentNullException(nameof(jwtOptions));
    }

    [HttpPost("[action]")]
    public async Task<IActionResult> SignUp(SignUpRequest register)
    {
        var user = new IdentityUser
        {
            Email = register.Email,
            UserName = register.Email,
            NormalizedUserName = register.Name,
        };
        var result = await _userManager.CreateAsync(user, register.Password);
        if (result.Succeeded)
        {
            var code = await _userManager.GenerateEmailConfirmationTokenAsync(user);

            if ((await _userManager.ConfirmEmailAsync(user, code)).Succeeded)
            {
                var userCart = await _cartService.CreateNewCartForUserAsync(user.Email);
                await _userService.Create(user.Email, register.Name, userCart.CartId);
                return Ok("SUCCESS");
            }
            else
            {
                return BadRequest("FAILED");
            }
        }
        else
        {
            foreach (var error in result.Errors)
            {
                ModelState.AddModelError(error.Code, error.Description);
            }
            return BadRequest(ModelState);
        }
    }

    [HttpPost("[action]")]
    public async Task<IActionResult> SignIn(SignInRequest request)
    {
        // check if user exists with that email
        var user = await _userManager.FindByEmailAsync(request.Email);
        if (user is null)
        {
            return NotFound($"User with email {request.Email} not found!");
        }

        if (await _userManager.CheckPasswordAsync(user, request.Password))
        {
            if (await _userManager.IsEmailConfirmedAsync(user))
            {
                var token = GenerateToken(user);
                //return Ok($"Bearer {token}");
                return Ok(token);
            }

            return BadRequest("Email is not confirmed. Please go to your email account");
        }

        return Unauthorized("Password is not valid");
    }

    [AllowAnonymous]
    [HttpPost("contactsignup")]
    public async Task<IActionResult> AddUserToCart(int cartId, string email, string name, string password)
    {
        var user = new IdentityUser
        {
            Email = email,
            UserName = email,
            NormalizedUserName = name
        };
        var result = await _userManager.CreateAsync(user, password);
        if (result.Succeeded)
        {
            var code = await _userManager.GenerateEmailConfirmationTokenAsync(user);

            if ((await _userManager.ConfirmEmailAsync(user, code)).Succeeded)
            {
                await _cartService.AddUserToCartAsync(cartId, email, user.Email);
                await _userService.Create(user.Email, name, cartId);
                return Ok("SUCCESS");
            }
            else
            {
                return BadRequest("FAILED");
            }
        }
        else
        {
            foreach (var error in result.Errors)
            {
                ModelState.AddModelError(error.Code, error.Description);
            }
            return BadRequest(ModelState);
        }
    }

    [HttpGet]
    [Authorize]
    public async Task<IActionResult> GetCurrentUser()
    {
        var email = User.Claims.LastOrDefault(c => c.Type == "Email")?.Value;
        var name = User.Claims.LastOrDefault(c => c.Type == "Name")?.Value;
        var cartId = User.Claims.LastOrDefault(c => c.Type == "CartId")?.Value;
        var user = User.Claims.Current();
        user.CartItems = await _itemService.GetItemsFromCartAsync(int.Parse(cartId));
        return Ok(user);
    }

    private string GenerateToken(IdentityUser user)
    {
        IList<Claim> userClaims = new List<Claim>
            {
                new Claim("UserName", user.UserName),
                new Claim("Email", user.Email),
                new Claim("Name", user.NormalizedUserName)
            };

        return new JwtSecurityTokenHandler().WriteToken(new JwtSecurityToken(
            claims: userClaims,
            expires: DateTime.UtcNow.AddDays(1),
            signingCredentials: new SigningCredentials(_jwtOptions.SecurityKeyToken, SecurityAlgorithms.HmacSha256),
            issuer: _jwtOptions.Issuer,
            audience: _jwtOptions.Audience
            ));
    }
}