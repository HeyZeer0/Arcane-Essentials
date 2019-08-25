package com.favouritedragon.arcaneessentials.common.spell.fire;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityFireball;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class KaFrizz extends Spell {
	private static final String SIZE = "size";

	public KaFrizz() {
		super(ArcaneEssentials.MODID, "kafrizz", EnumAction.BOW, false);
		addProperties(DAMAGE, RANGE, BURN_DURATION, SIZE);
	}


	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return cast(world, caster, modifiers);
	}

	private boolean cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {
		float size = getProperty(SIZE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		int burnDuration = getProperty(BURN_DURATION).intValue() * (int) modifiers.get(SpellModifiers.POTENCY);
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GHAST_SHOOT, WizardrySounds.SPELLS,
				1.0F + world.rand.nextFloat(), 1.0F + world.rand.nextFloat(), false);
		if (!world.isRemote) {
			EntityFireball fireball = new EntityFireball(world);
			fireball.setCaster(caster);
			fireball.setSize(size);
			fireball.setLifetime(70);
			fireball.setDamage(damage);
			fireball.setKnockbackStrength((int) size * 2);
			fireball.setBurnDuration(burnDuration);
			fireball.aim(caster, getProperty(RANGE).floatValue() / 50, 0F);
			return world.spawnEntity(fireball);
		} else return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return cast(world, caster, modifiers);
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
		if (!world.isRemote) {
			float size = getProperty(SIZE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			Vec3i dir = direction.getDirectionVec();
			EntityFireball fireball = new EntityFireball(world);
			fireball.setSize(size);
			fireball.setDamage(damage);
			fireball.setPosition(x, y, z);
			fireball.shoot(dir.getX(), dir.getY(), dir.getZ(), getProperty(RANGE).floatValue(), 0F);
			world.spawnEntity(fireball);
			return true;
		}
		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}

	@Override
	public boolean canBeCastByDispensers() {
		return true;
	}
}
