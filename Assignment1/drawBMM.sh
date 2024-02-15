#set logscale x 2
unset label
#set key above
#set key outside top center horizontal font ",3"
set autoscale
set style line 1 linetype 2
set term pdf size 8,4
set output "BMMvsBMMT.pdf"
set xtics (1,2,4,8,10,12,14,16) font "Bold,7" offset 0, graph 0.05
set ytics font "Bold,7"
set size ratio 0.3
set xlabel "Number of Threads" font "Bold,9" offset 0,1.25
set ylabel "Run Time" font "Bold,9" offset 3,0
#set key box
set key outside horizontal font "Bold,8"
#unset tics
#set key at screen 0.77,0.98 font ",18" vertical sample 0.4 spacing 0.3 width 0.5 height 0.5 maxrows 1
#set key off
#set key on center top horizontal center samplen 0.3 spacing 0.5 width 0.3 height 0.3  box lw 1 font ",3"
#set key at 16,32
#set multiplot layout 2,2
# set yrange [0:500]
#set ylabel "Run Time" font ",3"
set title "Block Size 32 BMM" font "Bold,7" offset -1.0,-3.5
set size 0.9,0.9
plot "drawBMM32.dat" using 1:3 title "BMM 32 SIZE 1024" with linespoint lw 2 ps 0.25,\
"drawBMMT32.dat" using 1:3 title "BMM transpose 32 SIZE 1024" with linespoint lw 2 ps 0.25,\
# "drawBMM4.dat" using 1:4 title "2048" with linespoint lw 2 ps 0.25,\
# "100-0-0.dat" using 1:5 title "DVY" with linespoint lw 2 ps 0.25,\
# "100-0-0.dat" using 1:6 title "BCCO" with linespoint lw 2 ps 0.25,\
# "100-0-0.dat" using 1:3 title "CVM" with linespoint lw 2 ps 0.25 linecolor rgb "#999999",\
# "100-0-0.dat" using 1:4 title "4ST" with linespoint lw 2 ps 0.25 linecolor rgb "#B844010"
#unset yrange
# set yrange [0:120]
# set title "Block Size 8 BMM" font "Bold,7" offset -1.0,-3.5
# set size 0.9,0.9
# plot "drawBMM8.dat" using 1:2 notitle "FATCBST_8" with linespoint lw 2 ps 0.25,\
# "drawBMM8.dat" using 1:3 notitle "FATCBST_32" with linespoint lw 2 ps 0.25,\
# "drawBMM8.dat" using 1:4 notitle "FATCBST_128" with linespoint lw 2 ps 0.25,\
# # "70-20-10.dat" using 1:5 notitle "DVY" with linespoint lw 2 ps 0.25,\
# # "70-20-10.dat" using 1:6 notitle "BCCO" with linespoint lw 2 ps 0.25,\
# # "70-20-10.dat" using 1:7 notitle "CVM" with linespoint lw 2 ps 0.25 linecolor rgb "#999999",\
# # "70-20-10.dat" using 1:8 notitle "4ST" with linespoint lw 2 ps 0.25 linecolor rgb "#B844010"
# set title "Block Size 16 BMM" font "Bold,7" offset -1.0,-3.5
# set size 0.9,0.9
# plot "drawBMM16.dat" using 1:2 notitle "FATCBST_8" with linespoint lw 7 ps 0.25,\
# "drawBMM16.dat" using 1:3 notitle "FATCBST_32" with linespoint lw 7 ps 0.25,\
# "drawBMM16.dat" using 1:4 notitle "FATCBST_128" with linespoint lw 7 ps 0.25,\
# # # "50-25-25.dat" using 1:5 notitle "DVY" with linespoint lw 7 ps 0.25,\
# # # "50-25-25.dat" using 1:6 notitle "BCCO" with linespoint lw 7 ps 0.25,\
# # # "50-25-25.dat" using 1:7 notitle "CVM" with linespoint lw 7 ps 0.25 linecolor rgb "#999999",\
# # # "50-25-25.dat" using 1:8 notitle "4ST" with linespoint lw 7 ps 0.25 linecolor rgb "#B844010"
# set title "Block Size 32 BMM" font "Bold,7" offset -1.0,-3.5
# set size 0.9,0.9
# plot "drawBMM32.dat" using 1:2 notitle "FATCBST_8" with linespoint lw 7 ps 0.25,\
# "drawBMM32.dat" using 1:3 notitle "FATCBST_32" with linespoint lw 7 ps 0.25,\
# "drawBMM32.dat" using 1:4 notitle "FATCBST_128" with linespoint lw 7 ps 0.25,\
# # # "30-35-35.dat" using 1:5 notitle "DVY" with linespoint lw 7 ps 0.25,\
# # # "30-35-35.dat" using 1:6 notitle "BCCO" with linespoint lw 7 ps 0.25,\
# # # "30-35-35.dat" using 1:7 notitle "CVM" with linespoint lw 7 ps 0.25 linecolor rgb "#999999",\
# # # "30-35-35.dat" using 1:8 notitle "4ST" with linespoint lw 7 ps 0.25 linecolor rgb "#B844010"
# set title "Block Size 64 BMM" font "Bold,7" offset -1.0,-3.5
# set size 0.9,0.9
# plot "drawBMM64.dat" using 1:2 notitle "FATCBST_8" with linespoint lw 7 ps 0.25,\
# "drawBMM64.dat" using 1:3 notitle "FATCBST_32" with linespoint lw 7 ps 0.25,\
# "drawBMM64.dat" using 1:4 notitle "FATCBST_128" with linespoint lw 7 ps 0.25,\
# "0-50-50.dat" using 1:5 notitle "DVY" with linespoint lw 7 ps 0.25,\
# "0-50-50.dat" using 1:6 notitle "BCCO" with linespoint lw 7 ps 0.25,\
# "0-50-50.dat" using 1:7 notitle "CVM" with linespoint lw 7 ps 0.25 linecolor rgb "#999999",\
# "0-50-50.dat" using 1:8 notitle "4ST" with linespoint lw 7 ps 0.25 linecolor rgb "#B844010"
 # unset multiplot
