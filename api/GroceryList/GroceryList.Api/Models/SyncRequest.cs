using GroceryList.Domain;

namespace GroceryList.Api.Models;

public class SyncRequest
{
    public ICollection<Item> Items { get; set; }
}
