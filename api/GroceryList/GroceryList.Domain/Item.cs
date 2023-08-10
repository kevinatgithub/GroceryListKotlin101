namespace GroceryList.Domain;

public class Item
{
    public int Id { get; set; }
    public int CartId { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public Decimal? Price { get; set; }
    public int? AlternativeItemId { get; set; }
    public bool IsPrimary { get; set; }
    public short Status { get; set; }
}
