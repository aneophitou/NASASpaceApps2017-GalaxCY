R = 6371007.181; %Earth Radius
T = 1111950; %height & width of each MODIS tile in projection plane
x_min = -20015109; %western limit
y_max = 10007555; %northern limit
w = T/1200; %actual size of a 1km MODIS sinusoidal grid cell

x_start = 32.296334;
y_start = 35.033197;
x_interval_200m = 32.295561 - 32.292964;
y_interval_200m = 35.033278 - 35.031528;

file = 'Akamas3x4Test.csv';
firemap = csvread(file);
%Ok send every 6 hrs
%Get Ready To Burn every 5 mins
%I see Smoke Smoke Smoke Continuous (every xxxxx)
%I see flames flames flames (every xxxxx)
%Kamikaze!!! I am dead. (Andreas, preferably dark dark red color)

x_0 = [];
y_0 = [];
x_1 = [];
y_1 = [];
x_2 = [];
y_2 = [];
x_3 = [];
y_3 = [];
x_4 = [];
y_4 = [];

for i=1:(size(firemap,1)) %1:12
    for j = 1:(size(firemap,2)) %1:9
        switch firemap(i,j)
            case 0
                x_0 = [x_0 i];
                y_0 = [y_0 j];
            case 1
               x_1 = [x_1 i];
               y_1 = [y_1 j];
            case 2
                x_2 = [x_2 i];
                y_2 = [y_2 j];
            case 3
                x_3 = [x_3 i];
                y_3 = [y_3 j];
            case 4
                x_4 = [x_4 i];
                y_4 = [y_4 j];
        end
    end
end

x_0 = x_start + x_0 * x_interval_200m;
y_0 = y_start + y_0 * y_interval_200m;

x_1 = x_start + x_1 * x_interval_200m;
y_1 = y_start + y_1 * y_interval_200m;

x_2 = x_start + x_2 * x_interval_200m;
y_2 = y_start + y_2 * y_interval_200m;

x_3 = x_start + x_3 * x_interval_200m;
y_3 = y_start + y_3 * y_interval_200m;

x_4 = x_start + x_4 * x_interval_200m;
y_4 = y_start + y_4 * y_interval_200m;

dlmwrite('x_0.csv',y_0,'precision','%.6f');
dlmwrite('x_0.csv',x_0,'precision','%.6f');

dlmwrite('x_1.csv',y_1,'precision','%.6f');
dlmwrite('x_1.csv',x_1,'precision','%.6f');

dlmwrite('x_2.csv',y_2,'precision','%.6f');
dlmwrite('x_2.csv',x_2,'precision','%.6f');

dlmwrite('x_3.csv',y_3,'precision','%.6f');
dlmwrite('x_3.csv',x_3,'precision','%.6f');

dlmwrite('x_4.csv',y_4,'precision','%.6f');
dlmwrite('x_4.csv',x_4,'precision','%.6f');