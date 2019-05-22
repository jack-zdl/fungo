package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.Member;
import com.fungo.system.proxy.IGameProxyService;
import com.fungo.system.service.IMakeAndInviteGameService;
import com.fungo.system.service.IMemberIncentRuleRankService;
import com.fungo.system.service.IMemberService;
import com.fungo.system.service.MemberService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.FollowUserOutBean;
import com.game.common.dto.game.GameInviteDto;
import com.game.common.dto.mark.MakeInputPageDto;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MakeAndInviteServiceGameImpl implements IMakeAndInviteGameService {


    @Autowired
    private MemberService memberService;

    @Autowired
    private IMemberIncentRuleRankService IRuleRankService;

    @Autowired
    private IMemberService iMemberService;

    @Autowired
    private IGameProxyService iGameProxyService;

    @Override
    public FungoPageResultDto<FollowUserOutBean> getInviteUserList(String memberId, MakeInputPageDto inputPageDto) throws Exception {
        FungoPageResultDto<FollowUserOutBean> re = new FungoPageResultDto<FollowUserOutBean>();
        List<FollowUserOutBean> list = new ArrayList<FollowUserOutBean>();
        re.setData(list);
        Page<Member> page = this.memberService.selectPage(new Page<Member>(inputPageDto.getPage(), inputPageDto.getLimit()), new EntityWrapper<Member>().ne("id", memberId).orderBy("sort", false));
        List<Member> plist = page.getRecords();
        List<IncentRuleRank> levelRankList = IRuleRankService.getLevelRankList();
        ObjectMapper mapper = new ObjectMapper();
        for (Member member : plist) {
            FollowUserOutBean bean = new FollowUserOutBean();
            bean.setAvatar(member.getAvatar());
            bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
            bean.setFollowed(false);
            GameInviteDto param = new GameInviteDto();
            param.setMemberId(memberId);
            param.setInviteMemberId(member.getId());
            param.setGameId(inputPageDto.getGameId());
            param.setState(0);
            GameInviteDto one =  iGameProxyService.selectGameInvite(param); //gameInviteService.selectOne(new EntityWrapper<GameInvite>().eq("member_id", memberId).eq("game_id", inputPageDto.getGameId()).eq("invite_member_id", member.getId()).eq("state", 0));
            if (one == null) {
                bean.setFollowed(false);
            } else {
                bean.setFollowed(true);
            }
//			int actionCount = actionService.selectCount(new EntityWrapper<BasAction>().eq("target_id", memberId).eq("type", 5).eq("member_id", memberId).and("state != 1"));
//			if (actionCount > 0) {
//				bean.setFollowed(true);
//			} else {
//				bean.setFollowed(false);
//			}
            bean.setLevel(member.getLevel());
            bean.setMemberNo(member.getMemberNo());
            bean.setObjectId(member.getId());
            bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
            bean.setUsername(member.getUserName());
            bean.setSign(member.getSign());
            String rankImgs = iMemberService.getLevelRankUrl(member.getLevel(), levelRankList);
            ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
            bean.setDignityImg((String) urlList.get(0).get("url"));
            list.add(bean);
        }
        PageTools.pageToResultDto(re, page);
        return re;
    }


}
