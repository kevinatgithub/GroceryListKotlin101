using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace GroceryList.Repositories.Migrations
{
    public partial class morechangestodb : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "EWmail",
                table: "UserCarts",
                newName: "Email");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Email",
                table: "UserCarts",
                newName: "EWmail");
        }
    }
}
