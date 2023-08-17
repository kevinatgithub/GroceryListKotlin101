namespace GroceryList.Api.Models;

public class ContactSignupRequest
{
    public int CartId { get; set; }
    public string Email { get; set; }
    public string Name { get; set; }
    public string Password { get; set; }
}
