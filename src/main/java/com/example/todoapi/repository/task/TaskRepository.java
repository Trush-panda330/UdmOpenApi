package com.example.todoapi.repository.task;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * タスクに関連するデータベース操作を行うリポジトリインターフェース
 */
@Mapper
public interface TaskRepository {

    @Select("SELECT id, title FROM tasks LIMIT #{limit} OFFSET #{offset}")
    List<TaskRecord> selectList(int limit, long offset);

    /**
     * 指定されたタスクIDに対応するタスクをデータベースから取得する。<p></p>
     *
     * @param taskId 取得するタスクのID
     * @return タスクが存在する場合は該当する {@link TaskRecord}のOptional,
     * 存在しない場合は空のOptional
     * */
    @Select("SELECT id, title FROM tasks WHERE id = #{taskId}")
    Optional<TaskRecord> select(Long taskId);

    /* ➁それを解決するために引数に渡したタスクレコードのIDフィールドに
    オートインクリメントされた値をセットしてもらう
    ＠Optionsを使う*/
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO tasks (title) VALUES (#{title})")
    void insert(TaskRecord record);

}
    /*➀MyBatisではInsertのアノテーションが付いたメソッドの戻り値はvoid出なければいけない。
      そうするとオートインクリメントされた値を戻り値で取得できない。 */
