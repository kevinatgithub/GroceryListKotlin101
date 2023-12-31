﻿using GroceryList.Domain;

namespace GroceryList.Repositories.Interfaces;

public interface IUserCartRepository
{
    public Task<UserCart> CreateUserCartAsync(int cartId, string email, string createdBy, string? avatar);
    public Task RemoveUserCartAsync(int userCartId);
    public Task<UserCart?> GetUserCartByIdAndEmailAsync(int cartId, string email);
    public Task<ICollection<UserCart>> GetAllUsersFromCartAsync(int cartId);
    public Task<UserCart> GetUserCartByEmailAsync(string email);
}
