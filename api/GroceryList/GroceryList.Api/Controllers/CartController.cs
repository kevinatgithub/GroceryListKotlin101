using GroceryList.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Win32;

namespace GroceryList.Api.Controllers;

[Route("api/[controller]")]
[ApiController]
[Authorize]
public class CartController : ControllerBase
{
    private readonly UserManager<IdentityUser> userManager;
    private readonly ICartService _cartService;
    private readonly IItemService _itemService;

    public CartController(UserManager<IdentityUser> userManager, ICartService cartService, IItemService itemService)
    {
        this.userManager = userManager;
        _cartService = cartService ?? throw new ArgumentNullException(nameof(cartService));
        _itemService = itemService ?? throw new ArgumentNullException(nameof(itemService));
    }

    [HttpGet("{cartId}")]
    public async Task<IActionResult> Items(int cartId)
    {
        var items = await _itemService.GetItemsFromCartAsync(cartId);
        return Ok(items);
    }

    [HttpGet("{cartId}/users")]
    public async Task<IActionResult> GetCartUsers(int cartId)
    {
        var users = await _cartService.GetCartUsers(cartId);
        return Ok(users);
    }

    [HttpPost("{cartId}/users")]
    public async Task<IActionResult> AddUserToCart(int cartId, string email, string name, string password)
    {
        var user = new IdentityUser
        {
            Email = email,
            UserName = email,
            NormalizedUserName = name
        };
        var result = await userManager.CreateAsync(user, password);
        if (result.Succeeded)
        {
            var code = await userManager.GenerateEmailConfirmationTokenAsync(user);

            if ((await userManager.ConfirmEmailAsync(user, code)).Succeeded)
            {
                await _cartService.AddUserToCartAsync(cartId, email, user.Email);
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

    [HttpDelete("{cartId}/users")]
    public async Task<IActionResult> RemoveUserFromCart(int cartId, string email)
    {
        await _cartService.RemoveUserOfCartAsync(cartId, email);
        return Ok();
    }
}
