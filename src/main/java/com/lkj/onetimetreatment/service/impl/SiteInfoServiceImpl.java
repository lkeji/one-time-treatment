package com.lkj.onetimetreatment.service.impl;

import com.lkj.onetimetreatment.common.CommonResult;
import com.lkj.onetimetreatment.entity.wb0097EarthquakeHistoryLibrary;
import com.lkj.onetimetreatment.entity.wb0097EarthquakeMonitoring;
import com.lkj.onetimetreatment.mapper.ISiteInfoMapper;
import com.lkj.onetimetreatment.service.ISiteInfoServiceI;
import com.lkj.onetimetreatment.utils.ExcelUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaohe
 */
@Service
public class SiteInfoServiceImpl implements ISiteInfoServiceI {

    private static Logger logger = LoggerFactory.getLogger(SiteInfoServiceImpl.class);
    @Autowired
    private ISiteInfoMapper siteInfoDao;

    @Autowired
    private MongoTemplate mongoDbCRUDI;

    @Override
    public CommonResult importExcel(MultipartFile file) {
        List<Map<String, Object>> excelsMap = new ArrayList<>();
        InputStream in = null;
        try {
            in = file.getInputStream();
            List<List<Object>> excels = ExcelUtil.readExcel(in);
            for (int i = 0; i < excels.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                for (int j = 0; j < excels.get(i).size(); j++) {
                    switch (j) {
                        case 0:
                            map.put("title", excels.get(i).get(j));
                            break;
                        case 1:
                            map.put("context", excels.get(i).get(j));
                            break;
                        case 2:
                            map.put("create_time", excels.get(i).get(j));
                            break;
                        case 3:
                            map.put("url", excels.get(i).get(j));
                            break;
                        default:
                            break;
                    }

                }
                excelsMap.add(map);
            }
            this.siteInfoDao.addMultSiteInfo(excelsMap);

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
            wb0097EarthquakeMonitoring wb0097EarthquakeMonitoring = new wb0097EarthquakeMonitoring();
            for (int i = 0; i < excels.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                for (int j = 0; j < excels.get(i).size(); j++) {
                    switch (j) {
                        case 0:
                            wb0097EarthquakeMonitoring.setStationname(String.valueOf( excels.get(i).get(j)));
                            break;
                        case 1:
                            wb0097EarthquakeMonitoring.setAdrs(String.valueOf( excels.get(i).get(j)));
                            break;
                        case 2:
                            wb0097EarthquakeMonitoring.setPrna(String.valueOf( excels.get(i).get(j)));
                            break;
                        case 3:
                            wb0097EarthquakeMonitoring.setErar(String.valueOf( excels.get(i).get(j)));
                            break;
                        case 4:
                            wb0097EarthquakeMonitoring.setCede(String.valueOf( excels.get(i).get(j)));
                            break;
                        case 5:
                            wb0097EarthquakeMonitoring.setNumbering(String.valueOf( excels.get(i).get(j)));
                            break;
                        case 6:
                            wb0097EarthquakeMonitoring.setStationtype(String.valueOf( excels.get(i).get(j)));
                            break;
                        case 7:
                            wb0097EarthquakeMonitoring.setLoc(String.valueOf( excels.get(i).get(j)));
                            break;
                        default:
                            break;
                    }

                }
                wb0097EarthquakeMonitoring.setId(DigestUtils.md5Hex(excels.get(i)+""));
                mongoDbCRUDI.save(wb0097EarthquakeMonitoring);
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
            wb0097EarthquakeHistoryLibrary wb0097EarthquakeMonitoring = new wb0097EarthquakeHistoryLibrary();
            for (int i = 1; i < excels.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                for (int j = 1; j < excels.get(i).size(); j++) {
                    switch (j) {
//                        case 0:
//                            wb0097EarthquakeMonitoring.setStationname(String.valueOf( excels.get(i).get(j)));
//                            break;
                        case 1:
                            wb0097EarthquakeMonitoring.setTimeofoccurrence(String.valueOf( excels.get(i).get(j)));
                            break;
                        case 2:
                            wb0097EarthquakeMonitoring.setEarthquakesituation(String.valueOf( excels.get(i).get(j)));
                            break;
                        case 3:
                            wb0097EarthquakeMonitoring.setHistoricalsources(String.valueOf( excels.get(i).get(j)));
                            break;
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
                wb0097EarthquakeMonitoring.setEpicentertype("2");
                wb0097EarthquakeMonitoring.setId(DigestUtils.md5Hex(excels.get(i)+""));
                mongoDbCRUDI.save(wb0097EarthquakeMonitoring);
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
