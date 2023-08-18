using GroceryList.Api.Extensions;
using GroceryList.Api.Models;
using GroceryList.Domain;
using GroceryList.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace GroceryList.Api.Controllers;

[Route("api/[controller]")]
[ApiController]
[Authorize]
public class ItemsController : ControllerBase
{
    private readonly IItemService _itemService;

    public ItemsController(IItemService itemService)
    {
        _itemService = itemService ?? throw new ArgumentNullException(nameof(itemService));
    }

    [HttpPost]
    public async Task<IActionResult> CreateItem(CreateItemRequest model)
    {
        var user = User.Claims.Current();
        var item = new Item
        {
            AlternativeItemId = model.AlternativeItemId,
            CartId = user.CartId,
            Description = model.Description,
            IsPrimary = model.AlternativeItemId == 0,
            Name = model.Name,
            PricePerUnit = model.PricePerUnit,
            Quantity = model.Quantity,
            Status = (int)ItemStatus.DEFAULT,
            img = model.img,
            imgUrl = model.imgUrl,
        };

        await _itemService.CreateItemAsync(item);

        if (item.AlternativeItemId == 0)
        {
            item.AlternativeItemId = item.Id;

            _itemService.UpdateItem(item);
        }
        return Ok(item);
    }

    [HttpPut("{itemId}")]
    public async Task<IActionResult> UpdateItem(int itemId, UpdateItemRequest model)
    {
        var item = await _itemService.GetItemDetailsAsync(itemId);
        if (item == null)
        {
            return BadRequest();
        }
        item.Description = model.Description;
        item.Name = model.Name; 
        item.PricePerUnit = model.PricePerUnit;
        item.Quantity = model.Quantity;
        item.img = model.img;
        item.imgUrl = model.imgURL;

        _itemService.UpdateItem(item);
        return Ok(item);
    }

    [HttpGet("{itemId}")]
    public async Task<IActionResult> GetItem(int itemId)
    {
        var item = await _itemService.GetItemDetailsAsync(itemId);
        return Ok(item);
    }

    [HttpGet("{itemId}/alternatives")]
    public async Task<IActionResult> GetAlternativeItems(int itemId)
    {
        var item = await _itemService.GetItemDetailsAsync(itemId);
        if ( item == null )
        {
            return BadRequest();
        }
        //if (item.IsPrimary == false)
        //{
        //    return Ok(new List<Item>() { });
        //}
        var items = await _itemService.GetAlternatives((int)item.AlternativeItemId);
        return Ok(items.Where(i => i.Id != itemId));
    }

    [HttpPost("{itemId}/primary")]
    public async Task<IActionResult> MarkAsPrimary(int itemId)
    {
        var item1 = await _itemService.GetItemDetailsAsync(itemId);
        if ( item1  == null)
        {
            return BadRequest();
        }
        var alts = await _itemService.GetAlternatives((int)item1.AlternativeItemId);
        alts.Where(i => i.Id != item1.Id).ToList().ForEach(item =>
        {
            item.IsPrimary = false;
            item.AlternativeItemId = itemId;
            _itemService.UpdateItem(item);
        });
        item1.IsPrimary = true;
        item1.AlternativeItemId = itemId;
        _itemService.UpdateItem(item1);
        return Ok();
    }

    [HttpPost("{itemId}/done")]
    public async Task<IActionResult> MarkAsDone(int itemId)
    {
        var item1 = await _itemService.GetItemDetailsAsync(itemId);
        if (item1 == null)
        {
            return BadRequest();
        }
        var alts = await _itemService.GetAlternatives((int)item1.AlternativeItemId);
        alts.Where(i => i.Id != item1.Id).ToList().ForEach(item =>
        {
            item.IsPrimary = false;
            item.AlternativeItemId = itemId;
            _itemService.UpdateItem(item);
        });
        item1.IsPrimary = true;
        item1.AlternativeItemId = itemId;
        _itemService.UpdateItem(item1);
        await _itemService.MarkItemAsAsync(itemId, (short)ItemStatus.DONE);
        return Ok();
    }

    [HttpPost("{itemId}/notavailable")]
    public async Task<IActionResult> MarkAsNotAvailable(int itemId)
    {
        var item1 = await _itemService.GetItemDetailsAsync(itemId);
        if (item1 == null)
        {
            return BadRequest();
        }
        var alts = await _itemService.GetAlternatives((int)item1.AlternativeItemId);
        alts.Where(i => i.Id != item1.Id).ToList().ForEach(item =>
        {
            item.IsPrimary = false;
            item.AlternativeItemId = itemId;
            _itemService.UpdateItem(item);
        });
        item1.IsPrimary = true;
        item1.AlternativeItemId = itemId;
        _itemService.UpdateItem(item1);
        await _itemService.MarkItemAsAsync(itemId, (short)ItemStatus.NOT_AVAILABLE);
        return Ok();
    }

    [HttpDelete("{itemId}")]
    public async Task<IActionResult> DeleteItem(int itemId)
    {
        var item = await _itemService.GetItemDetailsAsync(itemId);
        if (item != null)
        {
            await _itemService.RemoveItemAsync(item);
        }
        return Ok();
    }
}
