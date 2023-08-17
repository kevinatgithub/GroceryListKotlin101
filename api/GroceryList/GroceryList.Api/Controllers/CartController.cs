using GroceryList.Api.Extensions;
using GroceryList.Api.Models;
using GroceryList.Domain;
using GroceryList.Services;
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
    private readonly IUserService _userService;

    public CartController(
        UserManager<IdentityUser> userManager,
        ICartService cartService,
        IItemService itemService,
        IUserService userService)
    {
        this.userManager = userManager;
        _cartService = cartService ?? throw new ArgumentNullException(nameof(cartService));
        _itemService = itemService ?? throw new ArgumentNullException(nameof(itemService));
        _userService = userService;
    }

    [HttpGet("items")]
    public async Task<IActionResult> Items()
    {
        var user = User.Claims.Current();
        var items = await _itemService.GetItemsFromCartAsync(user.CartId);
        return Ok(items);
    }

    [HttpPost("sync")]
    public async Task<IActionResult> SyncList(SyncRequest model)
    {
        var user = User.Claims.Current();
        await _itemService.SyncCartItemsAsync(user.CartId, model.Items);
        return Ok();
    }

    [HttpGet("users")]
    public async Task<IActionResult> GetCartUsers()
    {
        var user = User.Claims.Current();
        var users = await _cartService.GetCartUsersAync(user.CartId);
        return Ok(users);
    }

    [HttpDelete("users")]
    public async Task<IActionResult> RemoveUserFromCart(string email)
    {
        var user = User.Claims.Current();
        await _cartService.RemoveUserOfCartAsync(user.CartId, email);
        return Ok();
    }

    [HttpDelete]
    public async Task<IActionResult> ClearCart()
    {
        var user = User.Claims.Current();
        await _itemService.ClearItemsInCart(user.CartId);
        return Ok();
    }
}
