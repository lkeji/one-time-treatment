package com.lkj.onetimetreatment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lkj.onetimetreatment.common.CommonResult;
import com.lkj.onetimetreatment.mapper.ISiteInfoMapper;
import com.lkj.onetimetreatment.mapper.tchemicalenterprisesMapper;
import com.lkj.onetimetreatment.service.ISiteInfoServiceI;
import com.lkj.onetimetreatment.utils.ExcelUtil;
import com.lkj.onetimetreatment.utils.GpsFixerUtil;
import com.lkj.onetimetreatment.utils.OKHttpUtil;
import io.swagger.models.auth.In;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author xiaohe
 */
@Service
public class SiteInfoServiceImpl implements ISiteInfoServiceI {

    private static Logger logger = LoggerFactory.getLogger(SiteInfoServiceImpl.class);
    @Autowired
    private ISiteInfoMapper siteInfoDao;

//    @Autowired
//    private MongoTemplate mongoDbCRUDI;

    @Autowired
    private tchemicalenterprisesMapper tchemicalnterprisesMapper;
    @Autowired
    private ISiteInfoServiceI siteInfoService;

    @Override
    public CommonResult importExcel(MultipartFile file) {
        List<Map<String, Object>> excelsMap = new ArrayList<>();
        InputStream in = null;
        try {
            in = file.getInputStream();
            List<List<Object>> excels = ExcelUtil.readExcel(in);
            for (int i = 1; i <=70; i++) {
                Map<String, Object> map = new HashMap<>();
                for (int j = 0; j < excels.get(i).size(); j++) {
                    switch (j) {
                        case 0:
                            String naa=String.valueOf(excels.get(i).get(j));
                            map.put("name", naa);
                            break;
                        case 1:
                            String names=String.valueOf(excels.get(i).get(j));
                            String mapurl="https://restapi.amap.com/v3/geocode/geo";
                            Map par=new HashMap();
                            par.put("key","705fd6734dfd4abb29a8293b7dd9a60e");
                            par.put("address",names);
                            par.put("city","迪庆");
                            String s = OKHttpUtil.get(mapurl, par);
                            String add="";
                            String tt="";
                            Map parse = (Map) JSONObject.parse(s);
                            if (Integer.parseInt(parse.get("count").toString()) == 1) {
                                List<Map> lists = (List<Map>)parse.get("geocodes");
                                Map tempMap=(Map)lists.get(0);
                                add= tempMap.get("formatted_address")+"";//地址
                                map.put("address", add);
                                String ioctem= tempMap.get("location")+"";//经纬度   99.705874,27.860035
                                String iocs[]=ioctem.split(",");
                                double[] loc=new double[]{Double.parseDouble(iocs[0]),Double.parseDouble(iocs[1])};
                                double[] doubles = GpsFixerUtil.gcj02_To_Gps84(loc);
                                tt= "["+String.valueOf(doubles[0])+","+String.valueOf(doubles[1])+"]";
                                map.put("loc", tt);
                            }
                            map.put("address", add);
                            map.put("loc", tt);
                            break;
                        case 2:
//                            map.put("create_time", excels.get(i).get(j));
                            break;
                        case 3:
//                            map.put("url", excels.get(i).get(j));
                            break;
                        case 4:
                            map.put("type", excels.get(i).get(j));
                            break;
                        case 5:
                            map.put("police", excels.get(i).get(j));
                            break;
                        default:
                            break;
                    }
                }
                excelsMap.add(map);
            }

            List<List<Object>> excelss = new ArrayList<>();
            for (Map<String, Object> map : excelsMap) {
                List<Object> row = new ArrayList<>();
                String name = map.get("name").toString();
                String address = map.get("address").toString();
                String loc = map.get("loc").toString();
                String type = map.get("type").toString();
                String police = map.get("police").toString();
                row.add(name);
                row.add(address);
                row.add(loc);
                row.add(type);
                row.add(police);
                excelss.add(row);
            }
            File file1=new File("D:\\tem\\jiayouzhan.xls");
            OutputStream fileOutputStream = new FileOutputStream(file1);
            ExcelUtil.exportExcel(excelss, "ggg", "xls", fileOutputStream);
            return CommonResult.success("上传成功");
        } catch (IOException e) {
            return CommonResult.failed("上传错误");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("导入 siteInfo excel 关流错误 : " + e.getMessage());
            }
        }
    }

    @Override
    public CommonResult getlocalioc(String key, String localName, String city,boolean ischage84) {
        Map<String, Object> map = new HashMap<>();
        String mapurl="https://restapi.amap.com/v3/geocode/geo";
        Map par=new HashMap();
        par.put("key",key);
        par.put("address",localName);
        par.put("city",city);
        String s = OKHttpUtil.get(mapurl, par);
        String add="";
        String tt="";
        Map parse = (Map) JSONObject.parse(s);
        if (Integer.parseInt(parse.get("count").toString()) == 1) {
            List<Map> lists = (List<Map>)parse.get("geocodes");
            Map tempMap=(Map)lists.get(0);
            add= tempMap.get("formatted_address")+"";//地址
            map.put("address", add);
            String ioctem= tempMap.get("location")+"";//经纬度   99.705874,27.860035
            String iocs[]=ioctem.split(",");
            if (!ischage84){
                tt= "["+iocs[0]+","+iocs[1]+"]";
            }else{
                double[] loc=new double[]{Double.parseDouble(iocs[0]),Double.parseDouble(iocs[1])};
                double[] doubles = GpsFixerUtil.gcj02_To_Gps84(loc);
                tt= "["+String.valueOf(doubles[0])+","+String.valueOf(doubles[1])+"]";
            }
        }
        map.put("address", add);
        map.put("loc", tt);

        return CommonResult.success(map,"转换成功");
    }

    @Override
    public CommonResult chage84(String lan, String lon, Integer mapType) {
        String tt="";
        if (mapType==1){
            double[] loc=new double[]{Double.parseDouble(lan),Double.parseDouble(lon)};
            double[] doubles = GpsFixerUtil.gcj02_To_Gps84(loc);
            tt= "["+String.valueOf(doubles[0])+","+String.valueOf(doubles[1])+"]";
        }else{

        }
        return CommonResult.success(tt,"转换成功");
    }


    @Override
    public CommonResult ExceltoMysql(MultipartFile file) {
        List<Map<String, Object>> excelsMap = new ArrayList<>();
        InputStream in = null;
        try {
            in = file.getInputStream();
            List<List<Object>> excels = ExcelUtil.readExcel(in);
            for (int i = 3; i < excels.size(); i++) {
                Map<String, Object> map = new HashMap<>();
//                wb0097enterpriseDataExpand wb0097enterpriseDataExpand = new wb0097enterpriseDataExpand();
                String mysqlid =null;
                for (int j = 1; j < excels.get(i).size(); j++) {
                    switch (j) {
//                        case 0:
//                            map.put("title", excels.get(i).get(j));
//                            break;
                        case 1:
                            map.put("areaCode", excels.get(i).get(j));
                            break;
                        case 2:
                            map.put("companyName", excels.get(i).get(j));
                            break;
                        case 3:
                            map.put("addressRegistry", excels.get(i).get(j));
                            break;
                        case 4:
                            map.put("responsiblePerson", excels.get(i).get(j));
                            break;
                        case 5:
                            map.put("responsiblePhone", excels.get(i).get(j));
                            break;
//                        case 6:
//                            wb0097enterpriseDataExpand.setNormalre(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 7:
//                            wb0097enterpriseDataExpand.setIsmajorso(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 8:
//                            wb0097enterpriseDataExpand.setNumbertan(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 9:
//                            wb0097enterpriseDataExpand.setMstcla(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 10:
//                            wb0097enterpriseDataExpand.setIswirtmhd(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 11:
//                            map.put("companyScale", excels.get(i).get(j));
//                            break;
//                        case 12:
//                            map.put("peoplePractitioner", excels.get(i).get(j));
//                            break;
//                        case 13:
//                            wb0097enterpriseDataExpand.setRemk(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        default:
////                             mysqlid = DigestUtils.md5Hex(excels.get(i).get(2) + "---");
////                            excelsMap.get(i).put("id", mysqlid);
//                            break;
                    }

                }

                int companyName = tchemicalnterprisesMapper.selectonly22(String.valueOf(map.get("companyName")));
                if (companyName == 0) {
                 mysqlid = DigestUtils.md5Hex(map.get("companyName") + "---");
                    map.put("id", mysqlid);
                    map.put("companyCode", "");
                    map.put("jurisdictionAreCode", "350200");
                    excelsMap.add(map);
//                    wb0097enterpriseDataExpand.setId(DigestUtils.md5Hex(mysqlid));
//                    wb0097enterpriseDataExpand.setMysqlid(mysqlid);
                    //todo新增mogo
//                    mongoDbCRUDI.save(wb0097enterpriseDataExpand);
                } else {
                    System.out.println("..."+map.get("companyName"));
                }

            }
            tchemicalnterprisesMapper.addMultSiteInfo(excelsMap);

            return CommonResult.success("上传成功");
        } catch (IOException e) {
            return CommonResult.failed("上传错误");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("导入 siteInfo excel 关流错误 : " + e.getMessage());
            }
        }
    }

    @Override
    public CommonResult ExceltoMaogoDb(MultipartFile file) {
        InputStream in = null;
        try {
            in = file.getInputStream();
            List<List<Object>> excels = ExcelUtil.readExcel(in);
//            wb0097EarthquakeMonitoring wb0097EarthquakeMonitoring = new wb0097EarthquakeMonitoring();
            for (int i = 0; i < excels.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                for (int j = 0; j < excels.get(i).size(); j++) {
                    switch (j) {
//                        case 0:
//                            wb0097EarthquakeMonitoring.setStationname(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 1:
//                            wb0097EarthquakeMonitoring.setAdrs(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 2:
//                            wb0097EarthquakeMonitoring.setPrna(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 3:
//                            wb0097EarthquakeMonitoring.setErar(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 4:
//                            wb0097EarthquakeMonitoring.setCede(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 5:
//                            wb0097EarthquakeMonitoring.setNumbering(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 6:
//                            wb0097EarthquakeMonitoring.setStationtype(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 7:
//                            wb0097EarthquakeMonitoring.setLoc(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        default:
//                            break;
                    }

                }
//                wb0097EarthquakeMonitoring.setId(DigestUtils.md5Hex(excels.get(i) + ""));
//                mongoDbCRUDI.save(wb0097EarthquakeMonitoring);
            }
            return CommonResult.success("新增mongodb成功");
        } catch (IOException e) {
            return CommonResult.failed("上传错误");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("导入 siteInfo excel 关流错误 : " + e.getMessage());
            }
        }
    }

    @Override
    public CommonResult ExceltoMaogoDb2(MultipartFile file) {
        InputStream in = null;
        try {
            in = file.getInputStream();
            List<List<Object>> excels = ExcelUtil.readExcel(in);
//            wb0097EarthquakeHistoryLibrary wb0097EarthquakeMonitoring = new wb0097EarthquakeHistoryLibrary();
            for (int i = 1; i < excels.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                for (int j = 1; j < excels.get(i).size(); j++) {
                    switch (j) {
//                        case 0:
//                            wb0097EarthquakeMonitoring.setStationname(String.valueOf( excels.get(i).get(j)));
//                            break;
//                        case 1:
//                            wb0097EarthquakeMonitoring.setTimeofoccurrence(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 2:
//                            wb0097EarthquakeMonitoring.setEarthquakesituation(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 3:
//                            wb0097EarthquakeMonitoring.setHistoricalsources(String.valueOf(excels.get(i).get(j)));
//                            break;
//                        case 4:
//                            wb0097EarthquakeMonitoring.setEpicenterlat(String.valueOf( excels.get(i).get(j)));
//                            break;
//                        case 5:
//                            wb0097EarthquakeMonitoring.setEpicenterlon(String.valueOf( excels.get(i).get(j)));
//                            break;
//                        case 6:
//                            wb0097EarthquakeMonitoring.setMagnitude(String.valueOf( excels.get(i).get(j)));
//                            break;
//                        case 7:
//                            wb0097EarthquakeMonitoring.setReferencePlaceName(String.valueOf( excels.get(i).get(j)));
//                            break;
                        default:
                            break;
                    }
                }
//                wb0097EarthquakeMonitoring.setEpicentertype("2");
//                wb0097EarthquakeMonitoring.setId(DigestUtils.md5Hex(excels.get(i) + ""));
//                mongoDbCRUDI.save(wb0097EarthquakeMonitoring);
            }
            return CommonResult.success("新增mongodb成功");
        } catch (IOException e) {
            return CommonResult.failed("上传错误");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("导入 siteInfo excel 关流错误 : " + e.getMessage());
            }
        }
    }

    @Override
    public CommonResult exportExcel(String fileName, String excelFormat, OutputStream fileOut) {
        List<Map<String, Object>> allSiteInfos = this.siteInfoDao.getDataOfExportExcel();
        List<List<Object>> excels = new ArrayList<>();
        for (Map<String, Object> map : allSiteInfos) {
            List<Object> row = new ArrayList<>();
            String title = map.get("title").toString();
            String context = map.get("context").toString();
            String createTime = map.get("create_time").toString();
            String url = map.get("url").toString();
            if (context.length() > 3200) {
                context = context.substring(0, 3200);
            }
            row.add(title);
            row.add(context);
            row.add(createTime);
            row.add(url);
            excels.add(row);
        }
        ExcelUtil.exportExcel(excels, fileName, excelFormat, fileOut);
        return CommonResult.success("下载成功");
    }

}
