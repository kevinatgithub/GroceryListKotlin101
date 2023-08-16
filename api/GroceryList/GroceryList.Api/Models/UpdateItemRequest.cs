namespace GroceryList.Api.Models;

public class UpdateItemRequest
{
    public string Name { get; set; }
    public string Description { get; set; }
    public Decimal? Price { get; set; }
}
