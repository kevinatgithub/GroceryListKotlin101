using GroceryList.Domain;

namespace GroceryList.Repositories.Interfaces;

public interface IItemRepository
{
    public Task<ICollection<Item>> GetAllItemsFromCartAsync(int cartId);
    public Task<Item?> GetByIdAsync(int id);
    public Task<Item?> CreateItemAsync(Item item);
    public Item? UpdateItem(Item item);
    public Task<Item?> RemoveFromCart(int id);
    public Task RemoveItemsInCart(int cartId);
    public Task<ICollection<Item>> GetAlternativeItems(int id);
    public void RemoveItemsByAltId(int id);
}
