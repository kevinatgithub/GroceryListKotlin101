namespace GroceryList.Domain;

public class UserCart
{
    public int Id { get; set; }
    public int CartId { get; set; }
    public string Email { get; set; }
    public string CreatedBy { get; set; }
}
