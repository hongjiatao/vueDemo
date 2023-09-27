package self.cases.teams.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import self.cases.teams.dao.PayLogsDao;
import self.cases.teams.dao.TeamsDao;
import self.cases.teams.dao.UsersDao;
import self.cases.teams.entity.PayLogVos;
import self.cases.teams.entity.Teams;
import self.cases.teams.entity.Users;
import self.cases.teams.handle.CacheHandle;
import self.cases.teams.service.UsersService;
import self.cases.teams.utils.DateUtils;
import self.cases.teams.utils.IDUtils;
import self.cases.teams.msg.R;
import self.cases.teams.msg.PageData;

import self.cases.teams.entity.PayLogs;
import self.cases.teams.service.PayLogsService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统请求响应控制器
 * 缴费记录
 */
@Controller
@RequestMapping("/payLogs")
public class PayLogsController extends BaseController {

    protected static final Logger Log = LoggerFactory.getLogger(PayLogsController.class);

    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PayLogsService payLogsService;

    @Resource
    private PayLogsDao payLogsDao;

    @Resource
    private TeamsDao teamsDao;

    @Resource
    private UsersDao usersDao;


    @RequestMapping("")
    public String index() {

        return "pages/PayLogs";
    }

    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id) {

        Log.info("查找指定缴费记录，ID：{}", id);

        PayLogs payLogs = payLogsService.getOne(id);

        return R.successData(payLogs);
    }

    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException {
//        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
//        List<PayLogs> payLogs = payLogsDao.selectList(new LambdaQueryWrapper<PayLogs>().eq(PayLogs::getUserId, user.getId()));
        ArrayList<PayLogVos> payLogVos = new ArrayList<>();


        List<PayLogs> payLogs = payLogsDao.selectList(null);
       for(PayLogs temp:payLogs){
           PayLogVos vo = new PayLogVos();
           vo.setId(temp.getId());
           Teams teams = teamsDao.selectById(temp.getTeamId());
           if(ObjectUtils.isNotEmpty(teams)){
               vo.setName(teams.getName());
           }
           Users users = usersDao.selectById(temp.getUserId());
           if(ObjectUtils.isNotEmpty(users)){
               vo.setUsername(users.getName());
               vo.setUserSex(users.getGender());
               vo.setUserPhone(users.getPhone());
           }
           vo.setCreateTime(temp.getCreateTime());
           vo.setPayMoney(String.valueOf(temp.getTotal()));
           payLogVos.add(vo);
       }
        EasyExcel.write(response.getOutputStream())
                .autoCloseStream(Boolean.FALSE)
                .head(PayLogVos.class)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("社团缴费数据")
                .doWrite(payLogVos);
    }

    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, String teamName, String userName) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        if (user.getType() == 0) {

            Log.info("分页查看全部缴费记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = payLogsService.getPageInfo(pageIndex, pageSize, null, teamName, userName);

            return R.successData(page);
        } else if (user.getType() == 1) {

            Log.info("团队管理员查看缴费记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = payLogsService.getManPageInfo(pageIndex, pageSize, user.getId(), teamName, userName);

            return R.successData(page);
        } else {

            Log.info("分页用户相关缴费记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex,
                    pageSize, teamName, userName);

            PageData page = payLogsService.getPageInfo(pageIndex, pageSize, user.getId(), teamName, null);

            return R.successData(page);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public R addInfo( PayLogs payLogs) {

        payLogs.setId(IDUtils.makeIDByCurrent());
        payLogs.setCreateTime(DateUtils.getNowDate());

        Log.info("添加缴费记录，传入参数：{}", payLogs);

        payLogsService.add(payLogs);

        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    public R updInfo(PayLogs payLogs) {

        Log.info("修改缴费记录，传入参数：{}", payLogs);

        payLogsService.update(payLogs);

        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id) {

        Log.info("删除缴费记录, ID:{}", id);

        PayLogs payLogs = payLogsService.getOne(id);

        payLogsService.delete(payLogs);

        return R.success();
    }
}