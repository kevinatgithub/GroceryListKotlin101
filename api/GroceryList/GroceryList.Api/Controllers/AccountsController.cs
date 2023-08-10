using GroceryList.Api.Models;
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
public class AccountsController : ControllerBase
{
    private readonly UserManager<IdentityUser> _userManager;
    private readonly JwtOptions _jwtOptions;

    public AccountsController(UserManager<IdentityUser> userManager, IOptions<JwtOptions> jwtOptions)
    {
        _userManager = userManager ?? throw new ArgumentNullException(nameof(userManager));
        _jwtOptions = jwtOptions.Value ?? throw new ArgumentNullException(nameof(jwtOptions));
    }

    [HttpPost("[action]")]
    public async Task<IActionResult> SignUp(SignUpRequest register)
    {
        var user = new IdentityUser
        {
            Email = register.Email,
            UserName = register.Email,
            NormalizedUserName = register.Name
        };
        var result = await _userManager.CreateAsync(user, register.Password);
        if (result.Succeeded)
        {
            var code = await _userManager.GenerateEmailConfirmationTokenAsync(user);

            if ((await _userManager.ConfirmEmailAsync(user, code)).Succeeded)
            {
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
