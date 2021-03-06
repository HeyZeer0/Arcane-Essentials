package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

public class WaveOfRelief extends Spell {

	public WaveOfRelief() {
		super(ArcaneEssentials.MODID, "wave_of_relief", EnumAction.BOW, false);
		addProperties(EFFECT_RADIUS, HEALTH);
	}

	//TODO: Sounds

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float healAmount = modifiers.get(SpellModifiers.POTENCY) * getProperty(HEALTH).floatValue();
		float radius = modifiers.get(WizardryItems.range_upgrade) * getProperty(EFFECT_RADIUS).floatValue();

		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.SPHERE).clr(1.0F, 1.0F, 0.3F).pos(caster.getPositionVector()).time(7).scale(radius / 2).spawn(world);
		}

		List<EntityLivingBase> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, caster.getEntityBoundingBox().grow(radius));
		if (!nearby.isEmpty()) {
			for (EntityLivingBase ally : nearby) {
				if (ally.getTeam() != null && ally.getTeam() == caster.getTeam() || caster == ally) {
					Collection<PotionEffect> potions = ally.getActivePotionEffects();
					for (PotionEffect effect : potions) {
						if (effect.getPotion().isBadEffect()) {
							ally.removePotionEffect(effect.getPotion());
						}
					}
					ally.heal(healAmount);
					if (ally instanceof EntityPlayer) {
						((EntityPlayer) ally).getFoodStats().setFoodLevel(((EntityPlayer) ally).getFoodStats().getFoodLevel() + 4);
					}
					if (world.isRemote){
						ParticleBuilder.spawnHealParticles(world, ally);
					}
				}
			}
		}

		caster.swingArm(hand);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		float healAmount = modifiers.get(SpellModifiers.POTENCY) * getProperty(HEALTH).floatValue();
		float radius = modifiers.get(WizardryItems.range_upgrade) * getProperty(EFFECT_RADIUS).floatValue();

		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.SPHERE).clr(1.0F, 1.0F, 0.3F).pos(caster.getPositionVector()).time(7).scale(radius / 2).spawn(world);
		}

		List<EntityLivingBase> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, caster.getEntityBoundingBox().grow(radius));
		if (!nearby.isEmpty()) {
			for (EntityLivingBase ally : nearby) {
				if (ally.getTeam() != null && ally.getTeam() == caster.getTeam() || caster == ally) {
					Collection<PotionEffect> potions = ally.getActivePotionEffects();
					for (PotionEffect effect : potions) {
						if (effect.getPotion().isBadEffect()) {
							ally.removePotionEffect(effect.getPotion());
						}
					}
					ally.heal(healAmount);
					if (ally instanceof EntityPlayer) {
						((EntityPlayer) ally).getFoodStats().setFoodLevel(((EntityPlayer) ally).getFoodStats().getFoodLevel() + 4);
					}
					if (world.isRemote){
						ParticleBuilder.spawnHealParticles(world, ally);
					}
				}
			}
		}

		caster.swingArm(hand);
		return true;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
