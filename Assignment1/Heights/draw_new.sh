set autoscale
set term pdf size 8,1.5
set output "64.pdf" 
set xlabel "Fatnode size" font "Bold,9" offset 0,1
set ylabel "Height" font "Bold,9" offset 3,0
#set xtics font "Verdana,3"
#set xtics {1,2,4,8,16,32,64,"128" 128} font "Bold,4" offset 0, graph 0.05
set ytics font "Bold,7"
set xtics "1" 1,"2" 2,"4" 4,"8"8,"16"16,"32"32,"64"64,"128"128 rotate by 0 font "Bold,7" offset 0, graph 0.05
#set xtic rotate by -90 scale 0
#set ytics font ",3"
set key at screen 0.63,0.99 font "Bold,9" vertical sample 0.3 spacing 0.5 width 0.2 height 2.5 maxrows 1
set boxwidth 0.75
set style data histogram 
set style histogram cluster gap 1
set style fill solid border -1
set yrange [0:50]
set multiplot layout 1,2
set title "Random Input" font "Bold,7" offset -1.0,-1.0
set size 0.45,0.9
plot "64.dat"  using 2:xtic(1) title "without removeSkewness()", "64.dat" using 3:xtic(1) title "with removeSkewness()"
set yrange [0:10000]
set title "Sorted Input" font "Bold,7" offset -1.0,-1.0
set size 0.45,0.9
plot "64.dat"  using 4:xtic(1) notitle "without removeSkewness()", "64.dat" using 5:xtic(1) notitle "with removeSkewness()"
unset multiplot
