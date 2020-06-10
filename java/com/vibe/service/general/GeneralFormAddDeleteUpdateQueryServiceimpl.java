package com.vibe.service.general;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.general.GeneralFormAddDeleteUpdateQueryDao;
import com.vibe.pojo.GeneralForm;


@Service
public class GeneralFormAddDeleteUpdateQueryServiceimpl implements GeneralFormAddDeleteUpdateQueryService {

	@Autowired
	private GeneralFormAddDeleteUpdateQueryDao dao;
	
	@Override
	public List<GeneralForm> queryForms(String business) {
		// TODO Auto-generated method stub
		
		return dao.queryForms(business);
	}

	@Override
	public void deleteForm(List<GeneralForm> generalForms) {
		// TODO Auto-generated method stub
		for (GeneralForm generalForm : generalForms) {
			dao.deleteForm(generalForm.getId(), generalForm.getBusiness());
		}
	}

	@Override
	public List<GeneralForm> insertForm(List<GeneralForm> generalForms) {
		// TODO Auto-generated method stub
		for (GeneralForm generalForm : generalForms) {
			dao.insertForm(generalForm);
		}
		return generalForms;
	}

	@Override
	public void updateForm(List<GeneralForm> generalForms) {
		// TODO Auto-generated method stub
		for (GeneralForm generalForm : generalForms) {
			dao.updateForm(generalForm.getData(), generalForm.getId());
		}
	}
	
}
