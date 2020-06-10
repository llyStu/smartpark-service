package com.vibe.controller.energy;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.vibe.common.Application;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Probe;


@Controller
public class EnergyManageController {
    @Autowired
    AssetStore store;

    @Autowired
    Application app;


    @RequestMapping(value = "/topage/energy/epublic", method = RequestMethod.GET)
    public String getMonitorList(ModelMap model) throws JSONException {
        //List<Map<String, Object>> results = new LinkedList<Map<String, Object>>();
        List<String> jsonList = new LinkedList<String>();
        List<Integer> idList = new LinkedList<Integer>();
        for (Asset<?> asset : store.getAssets()) {
            if (asset.getKind() != AssetKind.PROBE)
                continue;
            Probe probe = (Probe) asset;

            final String name = asset.getName();
            boolean isCurrent = name.contains("Current");
            boolean isVoltage = name.contains("Voltage");
            boolean isPower = name.contains("Power");
            boolean isRate = name.contains("PowerFactor") || name.contains("WayCT");
            if (isCurrent || isVoltage || isPower || isRate) {
                idList.add(probe.getId());

                final Float minValue = probe.getMinValue();
                final Float maxValue = probe.getMaxValue();
                String unit = "/";
                if (isCurrent)
                    unit = "A";
                else if (isVoltage)
                    unit = "V";
                else if (isPower)
                    unit = "P";

                JSONObject json = new JSONObject();
                json.put("id", probe.getId());
                json.put("caption", probe.getParent().getCaption() + probe.getCaption());
                if (probe.getValue() == null) {
                    json.put("value", "");
                } else {
                    json.put("value", probe.getValue());
                }
                json.put("unit", unit);
                json.put("minValue", minValue == null ? 0 : minValue);  //TODO:
                json.put("maxValue", maxValue == null ? 1000 : maxValue);//TODO:
                jsonList.add(json.toString());
            }
        }
        model.addAttribute("idList", idList);
        model.addAttribute("jsonList", jsonList);
        return "energy/probe_list";
    }
}
