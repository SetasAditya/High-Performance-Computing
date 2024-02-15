#set logscale x 2
unset label
#set key above
#set key outside top center horizontal font ",3"
set autoscale
#set style line 1 linetype 2
# set xrange [0.8:800]
set term pdf size 5,2.5
set output "BMMvsOMM.pdf"
#set xtics ("1" 1,"2" 2,"4" 4,"8" 8,"16" 16,"32" 32,"64" 64,"128" 128) rotate by -90 font "bold,3" offset 0, graph 0.05
set xtics ("1" 1,"2" 2,"4" 4,"6" 6,"8" 8,"10" 10,"12" 12,"14" 14,"16" 16) font "bold,9" offset 0, graph 0.05
set ytics font ",3"
#set size ratio 0.3
set xlabel "Number of Threads" font "Bold,9" offset 0, 1
set ylabel "Run Time" font "bold,9" offset 3, 0
set key outside horizontal font "Bold,8"
set boxwidth 0.5
set style data histograms
set style fill solid border 3
set multiplot layout 1,2
set title "BMM 2048 Matrix Size" font ",9" offset -0.9,-0.9
set size 0.50,0.70
plot "BMMvsOMM.dat" using 1:2 title "BMM" with boxes ls 0.25 lt rgb "#FF0000"
# plot "drawBMM4.dat" using 1:2 notitle with boxes ls 0.25 lt rgb "#FF0000"
# set title "BMM 8" font ",3" offset -0.9,-0.9
# set size 0.333,0.55
set title "OMM 2048 Matrix Size" font ",9" offset -0.9,-0.9
set size 0.50,0.70
plot "OMMvsOMMt.dat" using 1:3 title "OMM" with boxes ls 0.25 lt rgb "#999999"
# set title "BMM 16" font ",3" offset -0.9, -0.9
# set size 0.333,0.55
# set title "OMM 2048 Matrix Size" font ",9" offset -0.9,-0.9
# set size 0.333,0.55
# plot "drawOMM.dat" using 1:4 title "2048" with boxes ls 0.25 lt rgb "#B844010"
# set title "BMM 32" font ",3" offset -0.9, -0.9
# set size 0.333,0.55
# plot "drawBMM32.dat" using 1:4 title "uniform distribution" with boxes ls 0.25 lt rgb "#FF0000"
# set title "BMM 64" font ",3" offset -0.9, -0.9
# set size 0.333,0.55
# plot "drawBMM64.dat" using 1:4 title "uniform distribution" with boxes ls 0.25 lt rgb "#FF0000", "64_gaussian.dat" using 1:4 title "non-uniform distribution" with boxes ls 0.25 lt rgb "#00FF00"

unset multiplot
