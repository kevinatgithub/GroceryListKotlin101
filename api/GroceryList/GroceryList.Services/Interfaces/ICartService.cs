using GroceryList.Domain;

namespace GroceryList.Services.Interfaces;

public interface ICartService
{
    public Task<UserCart> CreateNewCartForUserAsync(string email);
    public Task AddUserToCartAsync(int cartId, string email, string addedBy, string? avatar);
    public Task RemoveUserOfCartAsync(int cartId, string email);
    public Task<ICollection<User>> GetCartUsersAync(int cartId);
}
