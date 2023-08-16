using GroceryList.Domain;
using GroceryList.Repositories.Interfaces;
using GroceryList.Services.Interfaces;

namespace GroceryList.Services;

public class CartService : ICartService, IUserCartService
{
    private readonly ICartRepository _cartRepository;
    private readonly IUserCartRepository _userCartRepository;

    public CartService(ICartRepository cartRepository, IUserCartRepository userCartRepository)
    {
        _cartRepository = cartRepository;
        _userCartRepository = userCartRepository;
    }

    public async Task AddUserToCartAsync(int cartId, string email, string addedBy)
    {
        await _userCartRepository.CreateUserCartAsync(cartId, email, addedBy);
    }

    public async Task CreateNewCartForUserAsync(string email)
    {
        var cart = await _cartRepository.CreateCartAsync(email);
        await _userCartRepository.CreateUserCartAsync(cart.Id, email, email);
    }

    public async Task<ICollection<UserCart>> GetCartUsers(int cartId)
    {
        return await _userCartRepository.GetAllUsersFromCartAsync(cartId);
    }

    public async Task RemoveUserOfCartAsync(int cartId, string email)
    {
        var uc = await _userCartRepository.GetUserCartByIdAndEmailAsync(cartId, email);
        if (uc != null)
        {
            await _userCartRepository.RemoveUserCartAsync(uc.Id);
        }
    }
}
