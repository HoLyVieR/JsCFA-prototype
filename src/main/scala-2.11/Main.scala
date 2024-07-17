import java.io._

/**
  * Created by Fei Peng on 6/8/16.
  */
object Main {
  def main(args: Array[String]) {
    if (args.length == 0) {
      println("Missing args")
    } else {
      val filename = args(0)
      val ast = GenerateAST(new File(filename))
      val decedAST = DecorateAST(ast)
      NameResolver(decedAST)
      val t1 = System.currentTimeMillis
      val disk = AAM.analyze(decedAST)
      val t2 = System.currentTimeMillis
      
      val w = new BufferedWriter(new FileWriter("edges.json"))
      w.write("[\n")
      var isFirst = true
      for (edge <- AAM.callEdges) {
        if (!isFirst) {
          w.write(",\n")
        }

        w.write("  [")
        w.write("\"" + filename + ":" + edge(0) + "\", ")
        w.write("\"" + filename + ":" + edge(1) + "\"")
        w.write("]")
        isFirst = false
      }
      w.write("\n]")
      w.close()

      /*println("AST :")
      DecorateAST.mapToAST.foreach{
        case (id, t) => println(id + " :: " + t.getClass)
      }

      println("Disk : ")
      disk.foreach{
        case (id, t) => println(id + " ->")
          for(v <- t) {
            println("    " + v + "\n")
          }
      }


      println("\n\nResult : ")
      DecorateAST.mapToAST.foreach {
        case (id, v) => if (id > 0 && v.isInstanceOf[IntroduceVar] && disk.contains(JSReference(id))) {
          val values = disk(JSReference(id))
          println(v.asInstanceOf[IntroduceVar].str + " -> ")
          values.foreach[Unit](x => println("    " + x))
        }
      }*/

      println((t2 - t1) + " msecs")
      println("Miss Match: " + AAM.missCount)
    }
  }
}
