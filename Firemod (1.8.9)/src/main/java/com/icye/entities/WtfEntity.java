package com.icye.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class WtfEntity extends EntityThrowable {

	public WtfEntity(World par1World) {
		super(par1World);
	}

	public WtfEntity(World par1World, EntityLivingBase par2EntityLivingBase) {
		super(par1World, par2EntityLivingBase);
	}

	public WtfEntity(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition movObjPos) {
		if (movObjPos.entityHit != null) {
			byte b0 = 100;
			movObjPos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) b0);
		}
		this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 4, true);
		for (int i = 0; i < 8; ++i) {
			this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}
		if (!this.worldObj.isRemote) {
			this.setDead();
		}
	}
}
