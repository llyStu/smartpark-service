package com.vibe.mapper.scene;

import com.vibe.pojo.Damage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DamageDao {

	public void insertDamage(Damage damage);

	public void deleteDamage(int id);

	public Damage queryDamage(int id);

	public List<Damage> queryDamageList();

	public void updateDamage(Damage damage);
	
}
