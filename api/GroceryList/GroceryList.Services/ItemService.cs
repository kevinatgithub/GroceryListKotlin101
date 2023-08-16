﻿using GroceryList.Domain;
using GroceryList.Repositories.Interfaces;
using GroceryList.Services.Interfaces;

namespace GroceryList.Services;

public class ItemService : IItemService
{
    private readonly IItemRepository _repository;

    public ItemService(IItemRepository repository)
    {
        _repository = repository;
    }

    public async Task<Item?> GetItemDetailsAsync(int id)
    {
        return await _repository.GetByIdAsync(id);
    }

    public async Task<ICollection<Item>> GetItemsFromCartAsync(int cartId)
    {
        return await _repository.GetAllItemsFromCartAsync(cartId);
    }

    public async Task<Item?> MarkItemAsAsync(int id, short status)
    {
        var item = await _repository.GetByIdAsync(id);
        if (item == null)
        {
            return null;
        }
        item.Status = status;
        _repository.UpdateItem(item);
        return item;
    }

    public async Task<Item?> RemoveItemAsync(Item item)
    {
        return await _repository.RemoveFromCart(item.Id);
    }

    public async Task<ICollection<Item>> SyncCartItemsAsync(ICollection<Item> items)
    {
        if (!items.Any())
        {
            return items;
        }
        var originalItems = await _repository.GetAllItemsFromCartAsync(items.FirstOrDefault().Id);
        if (originalItems.Any())
        {
            var oiids = originalItems.Select(oi => oi.Id); // original items ids
            var newItems = items.Where(i => !oiids.Contains(i.Id));
            newItems.ToList().ForEach(async item =>
            {
                await _repository.CreateItemAsync(item);
            });

            var toUpdate = items.Where(i =>  oiids.Contains(i.Id));
            toUpdate.ToList().ForEach(item =>
            {
                _repository.UpdateItem(item);
            });
            var iiids = items.Select(i2 => i2.Id); // input items ids
            var toRemove = originalItems.Where(i => !iiids.Contains(i.Id));
            toRemove.ToList().ForEach(async item =>
            {
                await _repository.RemoveFromCart(item.Id);
            });
            return await _repository.GetAllItemsFromCartAsync(items.FirstOrDefault().Id);
        }

        items.ToList().ForEach(async item =>
        {
            await _repository.CreateItemAsync(item);
        });

        return await _repository.GetAllItemsFromCartAsync(items.FirstOrDefault().Id);
    }

    public Item? UpdateItem(Item item)
    {
        return _repository.UpdateItem(item);
    }
}
