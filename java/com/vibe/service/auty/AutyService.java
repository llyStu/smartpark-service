package com.vibe.service.auty;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.vibe.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.auty.AutyMapper;
import com.vibe.pojo.auty.Abnormality;
import com.vibe.pojo.auty.ArrangInfo;
import com.vibe.pojo.auty.ArrangInfoConf;
import com.vibe.pojo.auty.ArrangInfoConfType;
import com.vibe.pojo.auty.Auty;
import com.vibe.pojo.auty.ChangeShifts;
import com.vibe.pojo.auty.ChangeShiftsGood;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;

@Service
public class AutyService {
	@Autowired
	private AutyMapper am;
	
	private static final String DAY_SHIFT = "DAY_SHIFT", TWO_SHIFTS = "TWO_SHIFTS", THREE_SHIFTS = "THREE_SHIFTS";

	private List<ArrangInfoConfType> types = Arrays.asList(
			new ArrangInfoConfType(DAY_SHIFT, "一天一班"),
			new ArrangInfoConfType(TWO_SHIFTS, "一天两班"),
			new ArrangInfoConfType(THREE_SHIFTS, "一天三班")
			);
	
	public static boolean isSkipDate(ZonedDateTime time, Set<Date> skipsDate) {
		boolean contains = skipsDate.contains(new Date(time.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000));
		return (time.getDayOfWeek() == DayOfWeek.SUNDAY || time.getDayOfWeek() == DayOfWeek.SATURDAY) ? !contains : contains;
	}
	
	private void insertArrangInfoSchedule(ArrangInfoConf conf) {
		ZonedDateTime start = conf.getStartTime().toInstant().atZone(ZoneId.systemDefault());
		ZonedDateTime end = conf.getStopTime().toInstant().atZone(ZoneId.systemDefault());

		String type = conf.getType().trim().toUpperCase();
		int[] staff = conf.getStaff();
		int confId = conf.getId();
		Set<Date> skipsDate = conf.getSkipsDate();
		
		switch (type) {
		default:
			return;
		case DAY_SHIFT: {
			int idx = -1;
			for (; start.isBefore(end); start=start.plusDays(1)) {
				if (isSkipDate(start, skipsDate)) continue;
				ArrangInfo info = new ArrangInfo();
				info.setDutyPeople(staff[++idx % staff.length]);
				info.setMemo(conf.getMemo() +"\r\n"+ start);
				info.setStartTime(new Date(start.toEpochSecond() * 1000));
				info.setStopTime(new Date(start.plusHours(8).toEpochSecond() * 1000));
				info.setConfId(confId);
				insertArrangInfo(info);
			}
		} break;
		case TWO_SHIFTS: {
			int idx = -1;
			while (start.isBefore(end)) {
				if (isSkipDate(start, skipsDate)) {
					start = start.plusHours(12);
					continue;
				}
				ArrangInfo info = new ArrangInfo();
				info.setDutyPeople(staff[++idx % staff.length]);
				info.setMemo(conf.getMemo() +"\r\n"+ start);
				info.setStartTime(new Date(start.toEpochSecond() * 1000));
				start = start.plusHours(12);
				info.setStopTime(new Date(start.toEpochSecond() * 1000));
				info.setConfId(confId);
				insertArrangInfo(info);
			}
		} break;
		case THREE_SHIFTS: {
			int idx = -1;
			while (start.isBefore(end)) {
				if (isSkipDate(start, skipsDate)) {
					start = start.plusHours(8);
					continue;
				}
				ArrangInfo info = new ArrangInfo();
				info.setDutyPeople(staff[++idx % staff.length]);
				info.setMemo(conf.getMemo() +"\r\n"+ start);
				info.setStartTime(new Date(start.toEpochSecond() * 1000));
				start = start.plusHours(8);
				info.setStopTime(new Date(start.toEpochSecond() * 1000));
				info.setConfId(confId);
				insertArrangInfo(info);
			}
		} break;
//		case FOUR_SHIFTS: {
//			int idx = -1;
//			while (start.isBefore(end)) {
//				if (isSkipDate(start, skipsDate)) {
//					start = start.plusHours(6);
//					continue;
//				}
//				ArrangInfo info = new ArrangInfo();
//				info.setDutyPeople(staff[++idx % staff.length]);
//				info.setMemo(conf.getMemo() +"\r\n"+ start);
//				info.setStartTime(new Date(start.toEpochSecond() * 1000));
//				start = start.plusHours(6);
//				info.setStopTime(new Date(start.toEpochSecond() * 1000));
//				info.setConfId(confId);
//				insertArrangInfo(info);
//			}
//		} break;
//		case FIVE_SHIFTS: {
//			int idx = -1;
//			while (start.isBefore(end)) {
//				if (isSkipDate(start, skipsDate)) {
//					start = start.plusHours((long) 4.8);
//					continue;
//				}
//				ArrangInfo info = new ArrangInfo();
//				info.setDutyPeople(staff[++idx % staff.length]);
//				info.setMemo(conf.getMemo() +"\r\n"+ start);
//				info.setStartTime(new Date(start.toEpochSecond() * 1000));
//				start = start.plusHours((long) 4.8);
//				info.setStopTime(new Date(start.toEpochSecond() * 1000));
//				info.setConfId(confId);
//				insertArrangInfo(info);
//			}
//		} break;
		
		}
	}
	public FormJson insertArrangInfoConfAndSchedule(ArrangInfoConf conf) {
		FormJson result = insertArrangInfoConf(conf);
		if (result.isSuccess()) {
			insertArrangInfoSchedule(conf);
		}
		return result;
	}
	public FormJson deleteArrangInfoConfAndSchedule(int[] ids) {
		FormJson deleteArrangInfoConf = deleteArrangInfoConf(ids);
		if (deleteArrangInfoConf.isSuccess()) {
			am.deleteArrangInfoByConf(ids);
		}
		return deleteArrangInfoConf;
	}
	public FormJson updateArrangInfoConfAndSchedule(ArrangInfoConf info) {
		FormJson updateArrangInfoConf = updateArrangInfoConf(info);
		if (updateArrangInfoConf.isSuccess()) {
			am.deleteArrangInfoByConf(new int[] {info.getId()});
			insertArrangInfoSchedule(info);
		}
		return updateArrangInfoConf;
	}
	
	
	public FormJson insertArrangInfoConf(ArrangInfoConf conf) {
		int result = am.insertArrangInfoConf(conf);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success().withCause(""+conf.getId());
	}
	public FormJson insertArrangInfoConfType1(int id,String type, String[] startime, String[] endtime, String memo) {//-------新增--------
		// TODO Auto-generated method stub
		int  result = am.insertArrangInfoConfType1(id,type, startime, endtime, memo);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success();
	}
	public FormJson deleteArrangInfoConf(int[] ids) {
		int result = am.deleteArrangInfoConf(ids);
		if (result == 0) {
			return FormJsonBulider.fail("0");
		}
		return FormJsonBulider.success().withCause(""+ result);
	}
	public FormJson deleteArrangInfoConfType1(int[] ids) {//--------删除-------
		int result = am.deleteArrangInfoConfType1(ids);
		if (result == 0) {
			return FormJsonBulider.fail("0");
		}
		return FormJsonBulider.success().withCause(""+ result);
	}
	public FormJson updateArrangInfoConf(ArrangInfoConf info) {
		int result = am.updateArrangInfoConf(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success();
	}
	public FormJson updateArrangInfoConfType1(ArrangInfoConf info) {//----------------修改-----
		int result = am.updateArrangInfoConfType1(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success();
	}
	public Page<ArrangInfo> findArrangInfoConf(ArrangInfoConf info, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ArrangInfo> list = am.findArrangInfoConf(info);
		return toPage(list);
	}
	public Page<ArrangInfo> findArrangInfoConfType1(ArrangInfoConf info, int pageNum, int pageSize) {//-----查找全部-------
		PageHelper.startPage(pageNum, pageSize);
		List<ArrangInfo> list = am.findArrangInfoConfType1(info);
		return toPage(list);
	}
	public FormJson insertArrangInfo(ArrangInfo info) {
		int result = am.insertArrangInfo(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success().withCause(""+info.getId());
	}
	public FormJson deleteArrangInfo(int[] ids) {
		int result = am.deleteArrangInfo(ids);
		if (result == 0) {
			return FormJsonBulider.fail("0");
		}
		return FormJsonBulider.success().withCause(""+ result);
	}
	public FormJson updateArrangInfo(ArrangInfo info) {
		int result = am.updateArrangInfo(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success();
	}
	public Page<ArrangInfo> findArrangInfo(ArrangInfo info, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ArrangInfo> list = am.findArrangInfo(info);
		if(null != list && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				ArrangInfo arrangInfo = list.get(i);
				if (StringUtils.isNotBlank(arrangInfo.getStaff()) && !StringUtils.equals("",arrangInfo.getStaff())){
					arrangInfo.setDutyPeopleNameList(am.selectDutyPeopleListByIds(arrangInfo.getStaff()));
				}

			}
		}
		return toPage(list);
	}
	
	public FormJson insertAuty(Auty info) {
		int result = am.insertAuty(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success().withCause(""+info.getId());
	}
	public FormJson deleteAuty(int[] ids) {
		int result = am.deleteAuty(ids);
		if (result == 0) {
			return FormJsonBulider.fail("0");
		}
		return FormJsonBulider.success().withCause(""+ result);
	}
	public FormJson updateAuty(Auty info) {
		int result = am.updateAuty(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success();
	}
	public Page<Auty> findAuty(Auty info, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Auty> list = am.findAuty(info);
		return toPage(list);
	}
	
	public FormJson insertAbnormality(Abnormality info) {
		int result = am.insertAbnormality(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success().withCause(""+info.getId());
	}
	public FormJson deleteAbnormality(int[] ids) {
		int result = am.deleteAbnormality(ids);
		if (result == 0) {
			return FormJsonBulider.fail("0");
		}
		return FormJsonBulider.success().withCause(""+ result);
	}
	public FormJson updateAbnormality(Abnormality info) {
		int result = am.updateAbnormality(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success();
	}
	public Page<Abnormality> findAbnormality(Abnormality info, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Abnormality> list = am.findAbnormality(info);
		return toPage(list);
	}
	
	public FormJson insertChangeShifts(ChangeShifts info) {
		int result = am.insertChangeShifts(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success().withCause(""+info.getId());
	}
	public FormJson deleteChangeShifts(int[] ids) {
		int result = am.deleteChangeShifts(ids);
		if (result == 0) {
			return FormJsonBulider.fail("0");
		}
		return FormJsonBulider.success().withCause(""+ result);
	}
	public FormJson updateChangeShifts(ChangeShifts info) {
		int result = am.updateChangeShifts(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success();
	}
	public Page<ChangeShifts> findChangeShifts(ChangeShifts info, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ChangeShifts> list = am.findChangeShifts(info);
		return toPage(list);
	}
	
	public FormJson insertChangeShiftsGood(ChangeShiftsGood info) {
		int result = am.insertChangeShiftsGood(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success().withCause(""+info.getId());
	}
	public FormJson deleteChangeShiftsGood(int[] ids) {
		int result = am.deleteChangeShiftsGood(ids);
		if (result == 0) {
			return FormJsonBulider.fail("0");
		}
		return FormJsonBulider.success().withCause(""+ result);
	}
	public FormJson updateChangeShiftsGood(ChangeShiftsGood info) {
		int result = am.updateChangeShiftsGood(info);
		if (result != 1) {
			return FormJsonBulider.fail(""+ result);
		}
		return FormJsonBulider.success();
	}
	public Page<ChangeShiftsGood> findChangeShiftsGood(ChangeShiftsGood info, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ChangeShiftsGood> list = am.findChangeShiftsGood(info);
		return toPage(list);
	}
	
	private static <T> Page<T> toPage(List<T> list) {
		PageInfo<T> page = new PageInfo<>(list);
		Page<T> result = new Page<>();
		result.setRows(list);
		result.setPage(page.getPageNum());
		result.setSize(page.getPageSize());
		result.setTotal((int) page.getTotal());
		return result;
	}
	public List<ArrangInfoConfType> findArrangInfoConfType() {
		return types;
	}

	
}
