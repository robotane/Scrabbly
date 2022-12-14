package com.robotane.game.scrabbly.ai;

import com.robotane.game.scrabbly.game.Board;
import com.robotane.game.scrabbly.game.Player;
import com.robotane.game.scrabbly.game.pieces.PieceType;

public class Algorithms {
    //minimax(game_stat: GameData, current_player, depth, max_player, game)
    public Algorithms(Board board, int level) {
    }

    public static void minimax(Board board, int depth, PieceType maxPlayer){
        int maxEval;
        if (depth == 0){
            
        }
        if (maxPlayer == PieceType.DARK){
            maxEval = Integer.MAX_VALUE;
            Player p = board.players[PieceType.DARK.ordinal()];
            p.getPieces();
        }
        /*
        if max_player:
        max_eval = float('-inf')
        best_game = None
        all_moves = get_all_played_moves(game_stat, current_player)
        all_moves.extend(get_all_taken_moves(game_stat, current_player))
        for stat in all_moves:
        evaluation = minimax(stat, stat.next_player, depth - 1, False, game)
        max_eval = max(max_eval, evaluation[0])
        if max_eval == evaluation[0]:
        best_game = stat
        return max_eval, best_game
        */
    }
}
