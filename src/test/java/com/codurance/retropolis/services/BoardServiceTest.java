package com.codurance.retropolis.services;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.factories.BoardFactory;
import com.codurance.retropolis.repositories.BoardRepository;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

  public static final Long USER_ID = 1L;
  public static final Long COLUMN_ID = 1L;
  public static final Long BOARD_ID = 1L;

  @Mock
  private BoardRepository boardRepository;

  @Mock
  private BoardFactory boardFactory;

  @Mock
  private UserService userService;

  @Mock
  private UserBoardService userBoardService;

  private BoardService boardService;

  @BeforeEach
  void setUp() {
    boardService = new BoardService(boardRepository, boardFactory, userService, userBoardService);
  }

  @Test
  void returns_a_board() {
    String columnTitle = "Start";
    when(boardRepository.getBoard(BOARD_ID)).thenReturn(
        new Board(BOARD_ID, "test board", List.of(new Column(COLUMN_ID, columnTitle, emptyList()))));

    Board board = boardService.getBoard(BOARD_ID);

    verify(boardRepository).getBoard(BOARD_ID);
    assertEquals(BOARD_ID, board.getColumns().get(0).getId());
    assertEquals(columnTitle, board.getColumns().get(0).getTitle());
    assertEquals(0, board.getColumns().get(0).getCards().size());
  }

  @Test
  void creates_a_board() {
    String testBoard = "test board";
    String userEmail = "john.doe@codurance.com";
    NewBoardRequestObject requestObject = new NewBoardRequestObject(testBoard,
        userEmail);
    Board board = new Board(BOARD_ID, testBoard, Collections.emptyList());

    when(boardFactory.create(requestObject)).thenReturn(board);
    when(userService.findOrCreateBy(userEmail)).thenReturn(new User(USER_ID, userEmail));
    when(boardRepository.insert(board)).thenReturn(board);

    boardService.createBoard(requestObject);

    verify(boardFactory).create(requestObject);
    verify(boardRepository).insert(board);
    verify(userBoardService).addToBoard(USER_ID, board.getId());
  }
}
