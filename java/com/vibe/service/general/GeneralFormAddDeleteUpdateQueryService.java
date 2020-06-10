package com.vibe.service.general;

import java.util.List;

import com.vibe.pojo.GeneralForm;

public interface GeneralFormAddDeleteUpdateQueryService {

    public List<GeneralForm> queryForms(String business);

    public void deleteForm(List<GeneralForm> generalForms);

    public List<GeneralForm> insertForm(List<GeneralForm> generalForms);

    public void updateForm(List<GeneralForm> generalForms);
}
