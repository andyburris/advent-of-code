package com.andb.adventofcode.year2019.day15

import com.andb.adventofcode.year2019.common.Coordinate
import com.andb.adventofcode.year2019.common.Direction
import com.andb.adventofcode.year2019.common.newIOThread
import com.andb.adventofcode.year2019.common.toIntcode
import kotlinx.coroutines.*
import java.awt.*
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel

private val reader = File("src/com/andb/adventofcode/year2019/day15/input.txt").bufferedReader()
//private val testReader = File("src/com/andb/adventofcode/year2019/day15/test.txt").bufferedReader()
private const val WINDOW_SIZE = 800

fun main(){
    partTwo()
}

private fun partOne(){
    runRobot { free, location ->
        val size = calculateDistance(Coordinate(0, 0), free, location)
        println("Size = $size")
    }
}

private fun partTwo(){
    runRobot { free, location ->
        //only possibilities are dead ends since no loops
        val possibilities = free.filter { coord ->
            coord.surrounding().intersect(free).size == 1
        }
        val jobs: List<Deferred<Int>> = possibilities.map { coord ->
            println("starting job for $coord")
            CoroutineScope(Dispatchers.IO).async {
                return@async calculateDistance(coord, free, location)
            }
        }
        newIOThread {
            val lengths = jobs.awaitAll()
            val max = lengths.maxBy { it }
            println("longest = $max")
        }
    }
}

private fun runRobot(onFind: (free: List<Coordinate>, location: Coordinate)->Unit){
    val software = reader.toIntcode()
    val walls = mutableListOf<Coordinate>()
    val free = mutableListOf(Coordinate(0, 0))
    val robot = Robot(Coordinate(0, 0), Direction.NORTH)
    var oxygen: Coordinate? = null
    val camera = initDraw()
    var lastCW = true
    software.onOutput = {output->
        fun moveRobot(){
            robot.coordinate = robot.move()
            robot.direction = robot.direction.cw()

            //println("Moved to free at ${robot.coordinate}")
            free.addUnique(robot.coordinate)
            software.input = mutableListOf(robot.direction.backingInt.toLong())
        }

        when (output){
            0L->{
                walls.addUnique(robot.move())
                robot.direction = robot.direction.ccw()
                software.input = mutableListOf(robot.direction.backingInt.toLong())
            }
            1L->{
                moveRobot()
            }
            2L-> {
                moveRobot()
                println("oxygen found at ${robot.coordinate}")
                oxygen = robot.coordinate
                camera.oxygen = oxygen
            }
            else->{
                throw (Exception("error - output = $output"))
            }
        }
        camera.free = free
        camera.walls = walls
        camera.current = robot.coordinate
        camera.repaint()
        //Thread.sleep(200)

        val unfound = (-camera.size..camera.size).flatMap { y->
            (-camera.size..camera.size).mapNotNull { x ->
                val coord = Coordinate(x, y)
                val surround = coord.surrounding()
                return@mapNotNull when{
                    free.contains(coord) || walls.contains(coord) -> null
                    free.any { surround.contains(it) } -> coord
                    else -> null
                }
            }
        }
        //println("unfound: $unfound")
        val full = unfound.isEmpty()
        if (full){
            onFind.invoke(free, oxygen!!)
            software.end()
        }
    }
    software.run()
}

private fun initDraw(): Camera{
    val frame = JFrame()
    val panel = Camera(25)
    frame.apply{
        add(panel)
        title = "Camera"
        size = Dimension(WINDOW_SIZE, WINDOW_SIZE)
        setLocationRelativeTo(null)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }
    return panel
}

private class Camera(val size: Int) : JPanel(){
    var walls = listOf<Coordinate>()
    var free = listOf<Coordinate>()
    var current = Coordinate(0, 0)
    var oxygen: Coordinate? = null
    override fun paint(g: Graphics) {
        val g2d = g as Graphics2D
        val increment = WINDOW_SIZE/(size*2)
        for (y in -size until size){
            for (x in -size until size){
                g2d.color = when(Coordinate(x, y)){
                    current -> Color.RED
                    oxygen -> Color.GREEN
                    in walls -> Color.WHITE
                    in free -> Color.BLUE
                    else -> Color.BLACK
                }
                //Fill from -y since awt graphics count from top left, we count from bottom left
                g2d.fillRect(x * increment + WINDOW_SIZE/2, -y * increment + WINDOW_SIZE/2, increment, increment)
            }
        }
        g2d.color = Color.BLACK
    }
}


private fun calculateDistance(to: Coordinate, space: List<Coordinate>, oxygen: Coordinate): Int{
    val path = space.repeatedFilter { list, coord ->
        if (coord == to) return@repeatedFilter true
        val surround = coord.surrounding()
        //println("Surround for $coord = $surround")
        if (surround.any{it == oxygen}) return@repeatedFilter true

        //println("Intersect = $intersect \n")
        return@repeatedFilter list.intersect(surround).size >= 2
    }
    //println("Path = $path")
    return path.size
}


private fun test(){

}

private data class Robot(var coordinate: Coordinate, var direction: Direction) {
    fun move(direction: Direction = this.direction) = coordinate.move(direction)
}

private fun <T> Collection<T>.repeatedFilter(predicate: (Collection<T>, T)->Boolean): Collection<T>{
    var current = this
    var changes = 1
    while (changes > 0){
        changes = 0
        val new = mutableListOf<T>()
        for (element in current){
            if (predicate.invoke(current, element)){
                new.add(element)
            } else {
                changes++
            }
        }
        current = new
    }
    return current
}

private fun <T> MutableCollection<T>.addUnique(element: T){
    if (!contains(element)){
        add(element)
    }
}