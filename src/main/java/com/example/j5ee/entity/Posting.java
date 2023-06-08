package com.example.j5ee.entity;

import lombok.*;

import javax.persistence.Table;

/***
 * @author Urmeas
 * @date 2022/11/20 10:26 下午
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
@Table(name = "posting")
public class Posting {
   private int postid;
   private int id;
   private int pid;
   private int status;

   public Posting(int id, int pid,Integer status) {
      this.id = id;
      this.pid = pid;
      this.status = status;
   }
}
