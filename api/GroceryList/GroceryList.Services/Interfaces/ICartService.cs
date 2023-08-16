using GroceryList.Domain;

namespace GroceryList.Services.Interfaces;

public interface ICartService
{
    public Task CreateNewCartForUserAsync(string email);
    public Task AddUserToCartAsync(int cartId, string email, string addedBy);
    public Task RemoveUserOfCartAsync(int cartId, string email);
    public Task<ICollection<UserCart>> GetCartUsers(int cartId);
}
