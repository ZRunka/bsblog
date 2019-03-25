package com.zrk.bsblog.controller;

import com.zrk.bsblog.domain.User;
import com.zrk.bsblog.service.BlogService;
import com.zrk.bsblog.service.VoteService;
import com.zrk.bsblog.util.ConstraintViolationExceptionHandler;
import com.zrk.bsblog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;

/**
 * Hello World 控制器.
 */
@Controller
@RequestMapping("/votes")
public class VoteController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private VoteService voteService;

	//发表点赞
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Response> createVote(Long blogId) {

		try {
			blogService.createVote(blogId);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false,
					ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}

		return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
	}

	//删除点赞
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Long blogId) {

		boolean isOwner = false;
		User user = voteService.getVoteById(id).getUser();

		// 判断操作用户是否是点赞的所有者
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal !=null && user.getUsername().equals(principal.getUsername())) {
				isOwner = true;
			}
		}

		if (!isOwner) {
			return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
		}

		try {
			blogService.removeVote(blogId, id);
			voteService.removeVote(id);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}

		return ResponseEntity.ok().body(new Response(true, "取消点赞成功", null));
	}
 
}
 