package top.ltcnb.classmanager.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.ltcnb.classmanager.Entity.Permissions;
import top.ltcnb.classmanager.Mapper.StarsMapper;
import top.ltcnb.classmanager.Entity.Article;
import top.ltcnb.classmanager.Entity.Stars;
import top.ltcnb.classmanager.Entity.Thumbs;
import top.ltcnb.classmanager.Response.R;
import top.ltcnb.classmanager.Service.IArticleService;
import top.ltcnb.classmanager.Service.IPermissionsService;
import top.ltcnb.classmanager.Service.IStarsService;
import top.ltcnb.classmanager.Service.IThumbsService;
import top.ltcnb.classmanager.Util.MinioUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;


@RequestMapping("/article")
@RestController
public class ArticleController {
    @Value("${minio.endpoint}/${minio.articleBucket}/")
    private String basePath;
    @Value("${minio.articleBucket}")
    private String articleBucket;
    final MinioUtil minioUtil;
    final IArticleService articleService;
    final IStarsService starsService;
    final IThumbsService thumbsService;
    final IPermissionsService permissionsService;
    @Autowired
    StarsMapper starsMapper;

    @Autowired
    public ArticleController(IPermissionsService permissionsService, MinioUtil minioUtil, IArticleService articleService, IStarsService starsService, IThumbsService thumbsService) {
        this.minioUtil = minioUtil;
        this.articleService = articleService;
        this.starsService = starsService;
        this.thumbsService = thumbsService;
        this.permissionsService = permissionsService;
    }

    @PostMapping("postArticle")
    public R postArticle(HttpServletRequest request, @RequestParam String title, @RequestParam String content) {

        try {
            Permissions permissions;
            Long id = Long.valueOf(request.getHeader("id"));
            if ((permissions = permissionsService.getById(id)) == null || permissions.getSystemAdminister() == 0) {
                return R.failed("权限不足");
            }
            String name = minioUtil.upload(new ByteArrayInputStream(content.getBytes()), articleBucket);
            Article article = new Article();
            article.setPosterId(id);
            article.setTitle(title);
            article.setDescription(content.length() > 100 ? content.substring(0, 100) : content);
            article.setFileName(name);
            articleService.save(article);
        } catch (Exception e) {
            System.out.println(e);
            return R.failed("未知错误", e.toString());
        }
        return R.success("发布成功");
    }


    @GetMapping("/articleList/{number}")
    public R listArticle(@PathVariable Integer number) {
        try {
            Page<Article> articlePage = new Page<>(number, 10);
            Page<Article> page = new LambdaQueryChainWrapper<>(articleService.getBaseMapper()).orderByDesc(Article::getModifyTime).page(articlePage);
            List<Article> res = page.getRecords();
            return R.success(res);
        } catch (Exception e) {
            return R.failed("未知错误", e.toString());
        }
    }

    @PostMapping("/star")
    public R star(HttpServletRequest request, @RequestParam String articleName) {
        try {
            Stars star = new Stars();
            star.setStarUser(Long.valueOf(request.getHeader("id")));
            star.setArticleName(articleName);
            if (starsService.getOne(new LambdaQueryWrapper<Stars>().eq(Stars::getArticleName,star.getArticleName()).eq(Stars::getStarUser,star.getStarUser()))==null&& starsService.save(star)) {
                return R.success("收藏成功");
            } else return R.failed("已经收藏过了");
        } catch (Exception e) {
            System.out.println(e);
            return R.failed(e.toString());
        }
    }

    @PostMapping("/thumbUp")
    public R thumbUp(HttpServletRequest request, @RequestParam String articleName) {
        try {
            Thumbs thumb = new Thumbs();
            thumb.setThumbUser(Long.valueOf(request.getHeader("id")));
            thumb.setArticleName(articleName);

            if (thumbsService.getOne(new LambdaQueryWrapper<Thumbs>().eq(Thumbs::getThumbUser,thumb.getThumbUser()).eq(Thumbs::getArticleName,thumb.getArticleName()))==null&& thumbsService.save(thumb)) {
                return R.success("点赞成功");
            } else return R.failed("已经点过赞了");
        } catch (Exception e) {
            return R.failed(e.toString());
        }
    }

    @PostMapping("/cancelThumbUp")
    public R cancelStar(HttpServletRequest request, @RequestParam String articleName) {
        try {
            LambdaQueryWrapper<Thumbs> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Thumbs::getArticleName, articleName).eq(Thumbs::getThumbUser, request.getHeader("id"));
            if (thumbsService.remove(lqw)) {
                return R.success("取消收藏成功");
            } else return R.failed("非法操作");

        } catch (Exception e) {
            return R.failed(e.toString());
        }
    }

    @PostMapping("/cancelStar")

    public R cancelThumb(HttpServletRequest request, @RequestParam String articleName) {
        try {
            LambdaQueryWrapper<Stars> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Stars::getArticleName, articleName).eq(Stars::getStarUser, request.getHeader("id"));
            if (starsService.remove(lqw)) {
                return R.success("取消点赞成功");
            } else return R.failed("非法操作");

        } catch (Exception e) {
            return R.failed(e.toString());
        }
    }


    @PostMapping("/isStar")
    public R isStar(HttpServletRequest request, @RequestParam String articleName) {
        try {
            LambdaQueryWrapper<Stars> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Stars::getArticleName, articleName).eq(Stars::getStarUser, request.getHeader("id"));
            if (starsService.getOne(lqw) != null) {
                return R.success(true);
            } else return R.success(false);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.toString());
        }
    }

    @PostMapping("/isThumb")
    public R isThumb(HttpServletRequest request, @RequestParam String articleName) {
        try {
            LambdaQueryWrapper<Thumbs> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Thumbs::getArticleName, articleName).eq(Thumbs::getThumbUser, request.getHeader("id"));
            if (thumbsService.getOne(lqw) != null) {
                return R.success(true);
            } else return R.success(false);
        } catch (Exception e) {
            return R.failed(e.toString());
        }
    }


    @GetMapping("info/{filename}")
    public R<Article> getArticleInfo(@PathVariable String filename) {
        try {
            Article article = articleService.getOne(new LambdaQueryWrapper<Article>().eq(Article::getFileName, filename));
            article.setDescription(null);
            article.setFileName("/data/article/" + article.getFileName());
            return R.success(article);
        } catch (Exception e) {
            return R.failed(e.toString());
        }
    }

    @PostMapping("getMyArticle")
    public R<List<Article>> getMyArticle(HttpServletRequest request) {
        try {
            return R.success(articleService.list(new LambdaQueryWrapper<Article>().eq(Article::getPosterId, request.getHeader("id"))));
        } catch (Exception e) {
            System.out.println(e);

            return R.failed(null);
        }
    }

    @PostMapping("getStarList")
    public R<List<Article>> getStarList(HttpServletRequest request) {
        try {
            return R.success(starsMapper.getStarList(request.getHeader("id")));
        } catch (Exception e) {
            System.out.println(e);
            return R.failed(e.toString());
        }
    }
}
