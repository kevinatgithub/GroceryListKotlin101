using GroceryList.Services.Interfaces;
using System.Security.Claims;

namespace GroceryList.Api.Middlewares;

public class SampleMiddleware
{
    private readonly RequestDelegate next;

    public SampleMiddleware(RequestDelegate next)
    {
        this.next = next;
    }

    public async Task InvokeAsync(HttpContext httpContext, IUserService userService)
    {
        if (httpContext.User != null && httpContext.User.Identity.IsAuthenticated)
        {
            var email = httpContext.User.Claims.FirstOrDefault(c => c.Type == "Email")?.Value;
            var user = await userService.Get(email);
            if (user != null)
            {
                var claims = new List<Claim>
                {
                    new Claim("Name", user.Name),
                    new Claim("Avatar", user.Avatar == null ? string.Empty : user.Avatar),
                    new Claim("CartId", $"{user.CartId}")
                };

                var appIdentity = new ClaimsIdentity(claims);
                httpContext.User.AddIdentity(appIdentity);
            }
        }

        await next(httpContext);
    }
}