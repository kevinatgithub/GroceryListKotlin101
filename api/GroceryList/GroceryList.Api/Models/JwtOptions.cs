using Microsoft.IdentityModel.Tokens;

namespace GroceryList.Api;

public class JwtOptions
{
    public string SecurityKey { get; set; }
    public string Issuer { get; set; }
    public string Audience { get; set; }
    public SecurityKey SecurityKeyToken { get; set; }
    public string DefaultAuthenticateScheme { get; set; }
    public string DefaultChallengeScheme { get; set; }
    public string DefaultScheme { get; set; }
}
