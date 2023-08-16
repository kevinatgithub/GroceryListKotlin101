using GroceryList.Api.Models;
using GroceryList.Domain;
using System.Security.Claims;

namespace GroceryList.Api.Extensions;

public static class ClaimsExtensions
{
    public static UserInfoResponse Current(this IEnumerable<Claim> claims)
    {
        var email = claims.LastOrDefault(c => c.Type == "Email")?.Value;
        var name = claims.LastOrDefault(c => c.Type == "Name")?.Value;
        var cartId = claims.LastOrDefault(c => c.Type == "CartId")?.Value;
        var user = new UserInfoResponse
        {
            Email = email,
            Name = name,
            CartId = int.Parse(cartId)
        };
        return user;
    }
}
