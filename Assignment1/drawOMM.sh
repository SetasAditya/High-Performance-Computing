set logscale x 2
unset label
#set key above
#set key outside top center horizontal font ",3"
#set autoscale
#set style line 1 linetype 2
set term pdf size 8,4
set output "OMMvsBMMline.pdf"
set xtics (1,2,4,8,16,32,64) font "Bold,7" offset 0, graph 0.05
set ytics font "Bold,7"
set size ratio 0.3
set xlabel "Number of Threads" font "Bold,9" offset 0,1.25
set ylabel "Run Time" font "Bold,9" offset 3,0
#set key box
set key outside horizontal font ",8"
#unset tics
#set key at screen 0.77,0.98 font ",18" vertical sample 0.4 spacing 0.3 width 0.5 height 0.5 maxrows 1
#set key off
#set key on center top horizontal center samplen 0.3 spacing 0.5 width 0.3 height 0.3  box lw 1 font ",3"
#set key at 16,32
# set multiplot layout 1,3
# set yrange [0:500]
#set ylabel "Throughput (MOPS)" font ",3"
set title "BMM vs OMM size 2048" font "Bold,7" offset -1.0,-3.5
set size 0.9, 0.9
plot "BMMvsOMM.dat" using 1:2 title "BMM" with linespoint lw 2 ps 0.25,\
"BMMvsOMM.dat" using 1:3 title "OMM" with linespoint lw 2 ps 0.25,\
# "drawOMM.dat" using 1:4 title "2048" with linespoint lw 2 ps 0.25,\
# "100-0-0.dat" using 1:5 title "DVY" with linespoint lw 2 ps 0.25,\
# "100-0-0.dat" using 1:6 title "BCCO" with linespoint lw 2 ps 0.25,\
# "100-0-0.dat" using 1:3 title "CVM" with linespoint lw 2 ps 0.25 linecolor rgb "#999999",\
# "100-0-0.dat" using 1:4 title "4ST" with linespoint lw 2 ps 0.25 linecolor rgb "#B844010"
#unset yrange
# set yrange [0:120]
# set title "70C-20I-10D \n (20K)" font "Bold,7" offset -1.0,-3.5
# set size 0.22,0.9
# plot "70-20-10.dat" using 1:2 notitle "FATCBST_8" with linespoint lw 2 ps 0.25,\
# "70-20-10.dat" using 1:3 notitle "FATCBST_32" with linespoint lw 2 ps 0.25,\
# "70-20-10.dat" using 1:4 notitle "FATCBST_128" with linespoint lw 2 ps 0.25,\
# "70-20-10.dat" using 1:5 notitle "DVY" with linespoint lw 2 ps 0.25,\
# "70-20-10.dat" using 1:6 notitle "BCCO" with linespoint lw 2 ps 0.25,\
# "70-20-10.dat" using 1:7 notitle "CVM" with linespoint lw 2 ps 0.25 linecolor rgb "#999999",\
# "70-20-10.dat" using 1:8 notitle "4ST" with linespoint lw 2 ps 0.25 linecolor rgb "#B844010"
# set title "50C-25I-25D \n (20K)" font "Bold,7" offset -1.0,-3.5
# set size 0.22,0.9
# plot "50-25-25.dat" using 1:2 notitle "FATCBST_8" with linespoint lw 7 ps 0.25,\
# "50-25-25.dat" using 1:3 notitle "FATCBST_32" with linespoint lw 7 ps 0.25,\
# "50-25-25.dat" using 1:4 notitle "FATCBST_128" with linespoint lw 7 ps 0.25,\
# "50-25-25.dat" using 1:5 notitle "DVY" with linespoint lw 7 ps 0.25,\
# "50-25-25.dat" using 1:6 notitle "BCCO" with linespoint lw 7 ps 0.25,\
# "50-25-25.dat" using 1:7 notitle "CVM" with linespoint lw 7 ps 0.25 linecolor rgb "#999999",\
# "50-25-25.dat" using 1:8 notitle "4ST" with linespoint lw 7 ps 0.25 linecolor rgb "#B844010"
# set title "30C-35I-35D \n (20K)" font "Bold,7" offset -1.0,-3.5
# set size 0.22,0.9
# plot "30-35-35.dat" using 1:2 notitle "FATCBST_8" with linespoint lw 7 ps 0.25,\
# "30-35-35.dat" using 1:3 notitle "FATCBST_32" with linespoint lw 7 ps 0.25,\
# "30-35-35.dat" using 1:4 notitle "FATCBST_128" with linespoint lw 7 ps 0.25,\
# "30-35-35.dat" using 1:5 notitle "DVY" with linespoint lw 7 ps 0.25,\
# "30-35-35.dat" using 1:6 notitle "BCCO" with linespoint lw 7 ps 0.25,\
# "30-35-35.dat" using 1:7 notitle "CVM" with linespoint lw 7 ps 0.25 linecolor rgb "#999999",\
# "30-35-35.dat" using 1:8 notitle "4ST" with linespoint lw 7 ps 0.25 linecolor rgb "#B844010"
# set title "0C-50I-50D \n (20K)" font "Bold,7" offset -1.0,-3.5
# set size 0.22,0.9
# plot "0-50-50.dat" using 1:2 notitle "FATCBST_8" with linespoint lw 7 ps 0.25,\
# "0-50-50.dat" using 1:3 notitle "FATCBST_32" with linespoint lw 7 ps 0.25,\
# "0-50-50.dat" using 1:4 notitle "FATCBST_128" with linespoint lw 7 ps 0.25,\
# "0-50-50.dat" using 1:5 notitle "DVY" with linespoint lw 7 ps 0.25,\
# "0-50-50.dat" using 1:6 notitle "BCCO" with linespoint lw 7 ps 0.25,\
# "0-50-50.dat" using 1:7 notitle "CVM" with linespoint lw 7 ps 0.25 linecolor rgb "#999999",\
# "0-50-50.dat" using 1:8 notitle "4ST" with linespoint lw 7 ps 0.25 linecolor rgb "#B844010"
 # unset multiplot
