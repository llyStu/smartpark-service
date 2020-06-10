package com.vibe.service.scene;

import java.util.List;

import com.vibe.pojo.Damage;


public interface DamageService {
	
	public void insertDamage(Damage damage);

	public void deleteDamage(int i);

	public Damage queryDamage(int id);

	public List<Damage> queryDamageList();

	public void updateDamage(Damage damage);
	
}
