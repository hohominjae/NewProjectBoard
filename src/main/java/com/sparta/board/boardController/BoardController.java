package com.sparta.board.boardController;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final Map<Long, Board> boardList = new HashMap<>();

    @PostMapping("/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto){
        // RequestDto -> Entity
        Board board = new Board(requestDto);

        //Board Max id Check
        Long maxId = boardList.size() > 0 ? Collections.max(boardList.keySet()) +1 : 1;
        board.setId(maxId);

        //DB 저장
        boardList.put(board.getId(), board);

        //Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    @GetMapping("/boards")
    public List<BoardResponseDto> getBoards(){
        //Map to List
        List<BoardResponseDto> responseDtos = boardList.values().stream()
                .map(BoardResponseDto::new).toList();

        return responseDtos;
    }

    @PutMapping("/boards/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto){
        //해당 메모가 DB에 존재하는지 확인
        if (boardList.containsKey(id)){
            //Board 가져오기
            Board board = boardList.get(id);

            //board 수정
            board.update(requestDto);
            return board.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    @DeleteMapping("/boards/{id}")
    public Long deleteBoard(@PathVariable Long id){
        //해당 Board가 DB에 존재하는지 확인
        if(boardList.containsKey(id)){
            //해당 board 삭제
            boardList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모 존재하지 않습니다.");
        }
    }
}
