using GroceryList.Domain;
using GroceryList.Repositories.Interfaces;
using GroceryList.Services.Interfaces;

namespace GroceryList.Services;

public class CartService : ICartService, IUserCartService
{
    private readonly ICartRepository _cartRepository;
    private readonly IUserCartRepository _userCartRepository;
    private readonly IUserRepository _userRepository;

    public CartService(ICartRepository cartRepository, IUserCartRepository userCartRepository, IUserRepository userRepository)
    {
        _cartRepository = cartRepository;
        _userCartRepository = userCartRepository;
        _userRepository = userRepository;
    }

    public async Task AddUserToCartAsync(int cartId, string email, string addedBy, string? avatar)
    {
        await _userCartRepository.CreateUserCartAsync(cartId, email, addedBy, avatar);
    }

    public async Task<UserCart> CreateNewCartForUserAsync(string email)
    {
        var cart = await _cartRepository.CreateCartAsync(email);
        return await _userCartRepository.CreateUserCartAsync(cart.Id, email, email, "");
    }

    public async Task<ICollection<User>> GetCartUsersAync(int cartId)
    {
        var userCarts = await _userCartRepository.GetAllUsersFromCartAsync(cartId);
        return await _userRepository.GetUsers(userCarts.Select(u => u.Email));
    }

    public async Task<UserCart> GetUserCartAsync(string email)
    {
        return await _userCartRepository.GetUserCartByEmailAsync(email);
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
