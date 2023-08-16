using GroceryList.Domain;

namespace GroceryList.Services.Interfaces;

public interface IItemService
{
    public Task<ICollection<Item>> GetItemsFromCartAsync(int cartId);
    public Task<Item?> GetItemDetailsAsync(int id);
    public Task<ICollection<Item>> SyncCartItemsAsync(ICollection<Item> items);
    public Task<Item?> RemoveItemAsync(Item item);
    public Item? UpdateItem(Item item);
    public Task<Item?> MarkItemAsAsync(int id, short status);
}
