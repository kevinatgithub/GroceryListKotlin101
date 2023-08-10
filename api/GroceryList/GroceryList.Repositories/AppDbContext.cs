using GroceryList.Domain;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace GroceryList.Repositories;

public class AppDbContext : IdentityDbContext
{
    // Note: always run add-migration for DbContext and then you can add-migration using IdentityDbContext after
    public AppDbContext(DbContextOptions options) : base(options)
    {
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        foreach (var property in modelBuilder.Model.GetEntityTypes()
                .SelectMany(t => t.GetProperties())
                .Where(p => p.ClrType == typeof(decimal) || p.ClrType == typeof(decimal?)))
        {
            property.SetColumnType("decimal(18,2)");
        }
        base.OnModelCreating(modelBuilder);
    }

    public DbSet<Cart> Carts { get; set; }
    public DbSet<Item> Items { get; set; }
    public DbSet<UserCart> UserCarts { get; set; }
}
