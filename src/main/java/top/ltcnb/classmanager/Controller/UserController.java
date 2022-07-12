package top.ltcnb.classmanager.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.ltcnb.classmanager.Entity.User;
import top.ltcnb.classmanager.Response.R;
import top.ltcnb.classmanager.Service.IUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.UUID;


@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    IUserService iUserService;
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    public UserController(IUserService iUserService, StringRedisTemplate stringRedisTemplate) {
        this.iUserService = iUserService;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    /**
     * @return 返回用户id、token
     */
    @PostMapping("/login")
    public R<Object> login(@RequestParam String account, @RequestParam String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "password").eq("account", account);
        User user = iUserService.getOne(queryWrapper);
        if (user == null) return R.failed("账号不存在");
        if (!user.getPassword().equals(password)) return R.failed("密码错误");
        String token = UUID.randomUUID().toString();
        HashMap<String, Object> res = new HashMap<>();
        stringRedisTemplate.opsForValue().set(user.getId().toString(), token);
        res.put("id", user.getId());
        res.put("token", token);
        return R.success(res);
    }

    @PostMapping("/register")
    public R<String> register(@RequestParam String name, @RequestParam String account, @RequestParam String password) {
        User user = new User();
        user.setName(name);
        user.setAccount(account);
        user.setPassword(password);
        if (iUserService.save(user))
            return R.success("注册成功");
        return R.failed("注册失败");
    }

    @PostMapping("/info")
    public R queryInfo(HttpServletRequest request) {
        try {
            String id = request.getHeader("id");
            User user = iUserService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, id));
            user.setPassword(null);
            return R.success(user);
        } catch (Exception e) {
            System.out.println(e);
            return R.failed(e);
        }
    }

    @PostMapping("/authToken")
    public R testToken() {
        System.out.println("_______________________");
        return R.success(true);
    }

    /**
     * @param id 用户的id
     */
    @PostMapping("/quit")
    public R quit(@RequestParam String id) {
        if (Boolean.TRUE.equals(stringRedisTemplate.delete(id)))
            return R.success("退出成功");
        else return R.failed("退出失败");
    }

}
