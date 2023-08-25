package de.teamlapen.vampirism_integrations.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.mojang.datafixers.util.Either;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.core.ModRecipes;
import de.teamlapen.vampirism.recipes.AlchemicalCauldronRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

/**
 * Allows you to add or remove recipes for the alchemical cauldron.
 *
 * @docParam <recipetype:vampirism:alchemical_cauldron>
 */
@ZenRegister
@Document("mods/vampirism/AlchemicalCauldronRecipe")
@ZenCodeType.Name("mods.vampirism.AlchemicalCauldronRecipe")
public class CTAlchemicalCauldronRecipeManager implements IRecipeManager<AlchemicalCauldronRecipe> {

    @Override
    public RecipeType<AlchemicalCauldronRecipe> getRecipeType() {
        return ModRecipes.ALCHEMICAL_CAULDRON_TYPE.get();
    }

    /**
     * Adds a recipe that requires an item as fluid source
     *
     * @param recipePath The recipe name, without the resource mod id
     * @param category Cooking book category
     * @param result The recipes result
     * @param ingredients The item ingredient
     * @param fluid The fluid item ingredient
     * @param level The required hunter level to use this recipe
     * @param cookTime The cooking time
     * @param exp The awarded experience
     * @param skills The skills the player must have unlocked to use this recipe
     * @docParam recipePath "iron_sword"
     * @docParam category "misc"
     * @docParam result <item:minecraft:iron_sword>
     * @docParam ingredients <item:minecraft:iron_ingot>
     * @docParam fluid <item:minecraft:iron_ingot>
     * @docParam level 7
     * @docParam cookTime 200
     * @docParam exp 5
     * @docParam skills [<skill:vampirism:basic_alchemy>]
     */
    @ZenCodeType.Method
    public void addRecipe(String recipePath, CookingBookCategory category, IItemStack result, IIngredient ingredients, IIngredient fluid, int level, int cookTime, int exp, ISkill<?>[] skills) {
        ResourceLocation id = new ResourceLocation("crafttweaker", recipePath);
        AlchemicalCauldronRecipe recipe = new AlchemicalCauldronRecipe(id, "", category, ingredients.asVanillaIngredient(), Either.left(fluid.asVanillaIngredient()), result.getInternal(), skills, level, cookTime, exp);
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, recipe, ""));
    }

    /**
     * Adds a recipe that requires a fluid as fluid source
     *
     * @param recipePath The recipe name, without the resource mod id
     * @param category Cooking book category
     * @param result The recipes result
     * @param ingredients The item ingredient
     * @param fluid The fluid item ingredient
     * @param level The required hunter level to use this recipe
     * @param cookTime The cooking time
     * @param exp The awarded experience
     * @param skills The skills the player must have unlocked to use this recipe
     * @docParam recipePath "iron_sword"
     * @docParam category "misc"
     * @docParam result <item:minecraft:iron_sword>
     * @docParam ingredients <item:minecraft:iron_ingot>
     * @docParam fluid <fluid:minecraft:water>
     * @docParam level 7
     * @docParam cookTime 200
     * @docParam exp 5
     * @docParam skills [<skill:vampirism:basic_alchemy>]
     */
    @ZenCodeType.Method
    public void addRecipe(String recipePath, CookingBookCategory category, IItemStack result, IIngredient ingredients, IFluidStack fluid, int level, int cookTime, int exp, ISkill<?>[] skills) {
        ResourceLocation id = new ResourceLocation("crafttweaker", recipePath);
        AlchemicalCauldronRecipe recipe = new AlchemicalCauldronRecipe(id, "", category, ingredients.asVanillaIngredient(), Either.right(fluid.getInternal()), result.getInternal(), skills, level, cookTime, exp);
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, recipe, ""));
    }
}
