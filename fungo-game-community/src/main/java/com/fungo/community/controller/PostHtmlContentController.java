package com.fungo.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.feign.GameFeignClient;
import com.fungo.community.function.SerUtils;
import com.fungo.community.function.TemplateUtil;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.util.CommonUtils;
import com.game.common.util.emoji.EmojiDealUtil;
import com.game.common.util.emoji.FilterEmojiUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 *  社区文章-帖子html内容处理
 * @author
 *
 */
@Controller
public class PostHtmlContentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostHtmlContentController.class);

    @Autowired
    private CmmPostDaoService daoPostService;

    @Autowired
    private FungoCacheArticle fungoCacheArticle;


    //依赖游戏微服务
    @Autowired(required = false)
    private GameFeignClient gameFeignClient;


    /**
     * 帖子内容html
     * @param postId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/api/content/post/html/{postId}")
    public ModelAndView getPostContentHtml(@PathVariable("postId") String postId) throws Exception {

        LOGGER.info("------/api/content/post/html/{postId}:{}", postId);

        //内容
        String keyPrefixContent = FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_HTML_CONTENT + postId;
        String htmlContent = fungoCacheArticle.getIndexCacheWithStr(keyPrefixContent, "");

        //标题
        String keyPrefixTitle = FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_HTML_TITLE + postId;
        String htmlContentTitle = fungoCacheArticle.getIndexCacheWithStr(keyPrefixTitle, "");

        CmmPost post = null;
        if (StringUtils.isNotBlank(htmlContent) && StringUtils.isNotBlank(htmlContentTitle)) {
            // 有头图直接放行
            if(htmlContent.startsWith("<img")){
                htmlContent = FilterEmojiUtil.decodeEmoji(htmlContent);
                htmlContentTitle = FilterEmojiUtil.decodeEmoji(htmlContentTitle);

                return TemplateUtil.returnPostHtmlTemplate(htmlContent, htmlContentTitle);
            }else {
                // 修复 缓存可能对有头图的文章过滤掉头图
                 post = daoPostService.selectById(postId);
                String htmlOrigin = post.getHtmlOrigin();
                if(htmlOrigin!=null&&!htmlOrigin.startsWith("<img")){
                    htmlContent = FilterEmojiUtil.decodeEmoji(htmlContent);
                    htmlContentTitle = FilterEmojiUtil.decodeEmoji(htmlContentTitle);
                    return TemplateUtil.returnPostHtmlTemplate(htmlContent, htmlContentTitle);
                }
            }
        }
        if(post==null){
            post = daoPostService.selectById(postId);
        }

        if (post == null) {
            throw new Exception("帖子不存在");
        } else {
            List<String> asList = new ArrayList<>();

            if (post.getImages() != null) {
                asList = Arrays.asList(post.getImages().replace("]", "").replace("[", "").replace("\"", "").split(","));
            }
            String origin = post.getHtmlOrigin();
            origin = FilterEmojiUtil.decodeEmoji(origin);

            List<Map<String, Object>> gameMapList = new ArrayList<>();
            String gameList = post.getGameList();
            //更新游戏标签中的游戏评分
            if (post.getGameList() != null) {
                ObjectMapper mapper = new ObjectMapper();
                gameMapList = mapper.readValue(gameList, ArrayList.class);
                for (Map<String, Object> m : gameMapList) {

                    //获取游戏平均分
                    String gameId = (String) m.get("objectId");
                    double gameAverage = 0;
                    try {
                        gameAverage = gameFeignClient.selectGameAverage(gameId, 0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    m.put("rating", String.valueOf(gameAverage));
                    String category = (String) m.get( "category" );
                    List<String> categorys = Arrays.asList(category.split(","));
                    if(categorys.size() > 3){
                        m.put( "category", String.join(",", categorys.subList(0, 3)));
                    }
                }

                gameList = mapper.writeValueAsString(gameMapList);
            }
            htmlContent = SerUtils.getWatermarkImageContent(CommonUtils.filterWord(origin), asList, gameList);
            htmlContentTitle = FilterEmojiUtil.decodeEmoji(post.getTitle());

            String hexadecimalHtml = htmlContent;
            if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(htmlContent))) {
                hexadecimalHtml = FilterEmojiUtil.encodeEmoji(htmlContent);
            }
            String titleHtml = htmlContentTitle;
            if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(htmlContentTitle))) {
                titleHtml = FilterEmojiUtil.encodeEmoji(htmlContentTitle);
            }

            //redis cache
            //文章内容html-内容
//           fungoCacheArticle.excIndexCache(true, keyPrefixContent, "", hexadecimalHtml);
//            //文章内容html-标题
//            fungoCacheArticle.excIndexCache(true, keyPrefixTitle, "", titleHtml);

            fungoCacheArticle.setStringCache(keyPrefixContent, "", hexadecimalHtml);
            //文章内容html-标题
            fungoCacheArticle.setStringCache(keyPrefixTitle, "", titleHtml);

            return TemplateUtil.returnPostHtmlTemplate(htmlContent, htmlContentTitle);

        }

    }

//-------
}
