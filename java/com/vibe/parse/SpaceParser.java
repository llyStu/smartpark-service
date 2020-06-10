package com.vibe.parse;

import java.util.List;

public class SpaceParser extends BaseParser<Space> {

    public static final String OUPUT_FILE_NAME = "init_space.sql";

    public static final int NAME_INDEX = 0;
    public static final int CAPTION_INDEX = 1;
    public static final int PARENT_INDEX = 2;

    public static final int ROOT_PARENT = 0;

    public static List<Space> spaces;

    private int rowIndex = 0;

    @Override
    public Space createBean() {
        // TODO Auto-generated method stub
        return new Space();
    }

    @Override
    public void fillData(List<String> excelData, Space data) {
        // TODO Auto-generated method stub
        data.setName(excelData.get(NAME_INDEX));
        data.setCaption(excelData.get(CAPTION_INDEX));
        data.setSeqence(rowIndex++);

    }

    @Override
    public void setParent(List<List<String>> excelData, List<Space> data) {
        // TODO Auto-generated method stub
        spaces = data;
        for (int i = 0; i < excelData.size(); i++) {
            String parent = excelData.get(i).get(PARENT_INDEX);
            if (parent == null || "".equals(parent)) {
                data.get(i).setParent("" + ROOT_PARENT);
            } else {
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).getName().equals(parent)) {
                        data.get(i).setParent(data.get(j).getId());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String getOutputFileName() {
        // TODO Auto-generated method stub
        return OUPUT_FILE_NAME;
    }

	/*@Override
	public void addToRAM(Space t) {
		// TODO Auto-generated method stub
		//com.vibe.monitor.asset.Space space = AssetStoreSqlSession.getAssetStore().createAsset(new SpaceType(""), t.getId(), t.getName());
		
	}*/


}
