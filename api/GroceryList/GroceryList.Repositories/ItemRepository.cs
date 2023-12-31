﻿using GroceryList.Domain;
using GroceryList.Repositories.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace GroceryList.Repositories;

public class ItemRepository : IItemRepository
{
    private readonly AppDbContext _context;

    public ItemRepository(AppDbContext context)
    {
        _context = context ?? throw new ArgumentNullException(nameof(context));
    }

    public async Task<Item?> CreateItemAsync(Item item)
    {
        await _context.Items.AddAsync(item);
        _context.SaveChanges();
        return item;
    }

    public async Task<ICollection<Item>> GetAllItemsFromCartAsync(int cartId)
    {
        return await _context.Items.Where(i => i.CartId == cartId).ToListAsync();
    }

    public async Task<ICollection<Item>> GetAlternativeItems(int id)
    {
        return await _context.Items.Where(i => i.AlternativeItemId == id).ToListAsync();
    }

    public async Task<Item?> GetByIdAsync(int id)
    {
        return await _context.Items.FirstOrDefaultAsync(i => i.Id == id);
    }

    public async Task<Item?> RemoveFromCart(int id)
    {
        var item = await _context.Items.FirstOrDefaultAsync(item => item.Id == id);
        if (item != null)
        {
            _context.Items.Remove(item);
            _context.SaveChanges();
        }
        return item;
    }

    public void RemoveItemsByAltId(int id)
    {
        var items = _context.Items.Where(i => i.AlternativeItemId == id);
        _context.Items.RemoveRange(items);
        _context.SaveChanges();
    }

    public async Task RemoveItemsInCart(int cartId)
    {
        var items = await _context.Items.Where(i => i.CartId == cartId).ToListAsync();
        _context.Items.RemoveRange(items);
        _context.SaveChanges();
    }

    public Item? UpdateItem(Item item)
    {
        _context.Items.Update(item);
        _context.SaveChanges();
        return item;
    }
}
