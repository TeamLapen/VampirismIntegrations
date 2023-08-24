package de.teamlapen.vampirism_integrations.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.items.IWeaponTableRecipe;
import de.teamlapen.vampirism.core.ModRecipes;
import de.teamlapen.vampirism.recipes.ShapedWeaponTableRecipe;
import de.teamlapen.vampirism.recipes.ShapelessWeaponTableRecipe;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;

/**
 * Allows you to add or remove weapon table recipes.
 *
 * @docParam this <recipetype:vampirism:weapontable_crafting>
 */
@ZenRegister
@Document("mods/vampirism/WeaponTableRecipe")
@ZenCodeType.Name("mods.vampirism.WeaponTableRecipe")
public class CTWeaponTableRecipeManager implements IRecipeManager<IWeaponTableRecipe> {

    @Override
    public RecipeType<IWeaponTableRecipe> getRecipeType() {
        return ModRecipes.WEAPONTABLE_CRAFTING_TYPE.get();
    }

    /**
     * Adds a shapeless recipe to the weapon table
     *
     * @param recipePath The recipe name, without the resource mod id
     * @param category crafting book category
     * @param result The recipes result
     * @param ingredients The input items for the crafting grid
     * @param level The required hunter level to use this recipe
     * @param lava The amount of lava consumed by this recipe
     * @param skills The skills the player must have unlocked to use this recipe
     * @docParam recipePath "iron_sword"
     * @docParam category "misc"
     * @docParam result <item:minecraft:iron_sword>
     * @docParam ingredients [<item:minecraft:iron_ingot>,<item:minecraft:iron_ingot>,<item:minecraft:stick>]
     * @docParam level 8
     * @docParam lava 5
     * @docParam skills [<skill:vampirism:tech_weapons>]
     */
    @ZenCodeType.Method
    public void addShapeless(String recipePath, CraftingBookCategory category, IItemStack result, IIngredient[] ingredients, int level, int lava, ISkill<?>[] skills) {
        ResourceLocation id = new ResourceLocation("crafttweaker", recipePath);
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.addAll(Arrays.stream(ingredients).map(IIngredient::asVanillaIngredient).toList());
        //noinspection unchecked
        ShapelessWeaponTableRecipe recipe = new ShapelessWeaponTableRecipe(category, id, "", nonnulllist, result.getInternal(), level, lava, (ISkill<IHunterPlayer>[]) skills);
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, recipe, "shapeless"));
    }

    /**
     * Adds a shaped recipe to the weapon table
     *
     * @param recipePath The recipe name, without the resource mod id
     * @param category crafting book category
     * @param result The recipes result
     * @param ingredients The input items for the crafting grid
     * @param level The required hunter level to use this recipe
     * @param lava The amount of lava consumed by this recipe
     * @param skills The skills the player must have unlocked to use this recipe
     * @docParam recipePath "iron_sword"
     * @docParam category "misc"
     * @docParam result <item:minecraft:iron_sword>
     * @docParam ingredients [[air,<item:minecraft:iron_ingot>, air],[air,<item:minecraft:iron_ingot>, air],[air,<item:minecraft:stick>, air]]
     * @docParam level 8
     * @docParam lava 5
     * @docParam skills [<skill:vampirism:tech_weapons>]
     */
    @ZenCodeType.Method
    public void addShaped(String recipePath, CraftingBookCategory category, IItemStack result, IIngredient[][] ingredients, int level, int lava, ISkill<?>[] skills) {
        ResourceLocation id = new ResourceLocation("crafttweaker", recipePath);
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.addAll(Arrays.stream(ingredients).flatMap(Arrays::stream).map(IIngredient::asVanillaIngredient).toList());
        //noinspection unchecked
        ShapedWeaponTableRecipe recipe = new ShapedWeaponTableRecipe(id, "", category,  ingredients[0].length, ingredients.length, nonnulllist, result.getInternal(), level, (ISkill<IHunterPlayer>[]) skills, lava);
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, recipe, "shaped"));
    }
}
