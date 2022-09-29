package com.codestates.seb006main.members.mapper;

import com.codestates.seb006main.members.dto.BookmarkDto;
import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Bookmark;
import com.codestates.seb006main.members.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostToMember(MemberDto.Post post);
    MemberDto.Response memberToMemberResponse(Member member);
    Member memberPatchToMember(MemberDto.Patch patch);
    MemberDto.OAuthResponse memberToMemberOAuthResponse(Member member);
    default List<BookmarkDto.Response> bookmarksToResponses(List<Bookmark> bookmarks){
        if ( bookmarks == null ) {
            return null;
        }

        List<BookmarkDto.Response> list = new ArrayList<BookmarkDto.Response>( bookmarks.size() );
        for ( Bookmark bookmark : bookmarks ) {
            list.add( bookmarkToResponse( bookmark ) );
        }

        return list;
    }

    default BookmarkDto.Response bookmarkToResponse(Bookmark bookmark){
        if ( bookmark == null ) {
            return null;
        }

        BookmarkDto.Response.ResponseBuilder response = BookmarkDto.Response.builder();

        response.postId(bookmark.getPost().getPostId());

        return response.build();
    }
}
