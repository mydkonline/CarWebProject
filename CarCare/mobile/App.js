import React, {useEffect, useMemo, useState} from 'react';
import {
  ActivityIndicator,
  Image,
  Pressable,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  View,
  useWindowDimensions,
} from 'react-native';

// iOS simulator: http://localhost:18081
// Android emulator: http://10.0.2.2:18081
// Real device: http://<Mac LAN IP>:18081
const API_BASE_URL = process.env.EXPO_PUBLIC_API_BASE_URL || 'http://localhost:18081';

// React Native local images must be required with static paths.
// The image files were moved from Spring Boot static resources to mobile/assets/images.
const CAR_IMAGES = {
  LC500h: require('./assets/images/carList/LC500h.png'),
  UX250hFSPORT: require('./assets/images/carList/UX250hFSPORT.png'),
  LS500: require('./assets/images/carList/LS500.png'),
  ES300h: require('./assets/images/carList/ES300h.png'),
  NX450h: require('./assets/images/carList/NX450h.png'),
  RZ450e: require('./assets/images/carList/RZ450e.png'),
};

const HERO_IMAGE = require('./assets/images/carList/LC500h.png');
const ADMIN_IMAGE = require('./assets/images/logo/MVlogo3.png');
const SHOWCASE_IMAGES = [
  require('./assets/images/carList/CAR_E2.jpg'),
  require('./assets/images/carList/CAR_B.jpg'),
  require('./assets/images/service/service-image.png'),
];
const STEPS = ['모델', '트림', '전시장', '신청'];
const DATE_PAGE_SIZE = 7;

export default function App() {
  const {width} = useWindowDimensions();
  const isWide = width >= 900;

  const [cars, setCars] = useState([]);
  const [options, setOptions] = useState([]);
  const [centers, setCenters] = useState([]);
  const [viewMode, setViewMode] = useState('booking');
  const [activeStep, setActiveStep] = useState(0);
  const [selectedCar, setSelectedCar] = useState(null);
  const [selectedOption, setSelectedOption] = useState(null);
  const [selectedCenter, setSelectedCenter] = useState(null);
  const [datePage, setDatePage] = useState(0);
  const [reservationDate, setReservationDate] = useState(() => buildAvailableDates(0)[0].value);
  const [availability, setAvailability] = useState([]);
  const [reservationFeedback, setReservationFeedback] = useState(null);
  const [adminLoggedIn, setAdminLoggedIn] = useState(false);
  const [adminUsername, setAdminUsername] = useState('admin');
  const [adminPassword, setAdminPassword] = useState('admin123');
  const [adminSummary, setAdminSummary] = useState(null);
  const [adminReservations, setAdminReservations] = useState([]);
  const [adminProducts, setAdminProducts] = useState([]);
  const [adminBrands, setAdminBrands] = useState([]);
  const [adminModels, setAdminModels] = useState([]);
  const [brandName, setBrandName] = useState('');
  const [modelBrandId, setModelBrandId] = useState(null);
  const [modelName, setModelName] = useState('');
  const [optionCarId, setOptionCarId] = useState(null);
  const [optionGrade, setOptionGrade] = useState('');
  const [optionColor, setOptionColor] = useState('');
  const [optionCc, setOptionCc] = useState('');
  const [optionKm, setOptionKm] = useState('');
  const [optionPrice, setOptionPrice] = useState('');
  const [adminLoading, setAdminLoading] = useState(false);
  const [adminFeedback, setAdminFeedback] = useState(null);
  const [loading, setLoading] = useState(true);
  const [optionLoading, setOptionLoading] = useState(false);
  const [centerLoading, setCenterLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const dateOptions = useMemo(() => buildAvailableDates(datePage), [datePage]);

  // 1. App starts by loading only data from Spring Boot. UI and images stay in React Native.
  useEffect(() => {
    fetchJson('/api/cars')
      .then(setCars)
      .catch(showError)
      .finally(() => setLoading(false));
  }, []);

  // 2. A selected car controls the option list. Reset child selections to prevent stale reservation data.
  useEffect(() => {
    if (!selectedCar) {
      return;
    }

    setSelectedOption(null);
    setSelectedCenter(null);
    setCenters([]);
    setOptionLoading(true);
    fetchJson(`/api/cars/${selectedCar.carId}/options`)
      .then(setOptions)
      .catch(showError)
      .finally(() => setOptionLoading(false));
  }, [selectedCar]);

  // 3. A selected option controls available centers. The backend currently returns sampled centers.
  useEffect(() => {
    if (!selectedOption) {
      return;
    }

    setSelectedCenter(null);
    setCenterLoading(true);
    fetchJson(`/api/options/${selectedOption.optionId}/centers`)
      .then(setCenters)
      .catch(showError)
      .finally(() => setCenterLoading(false));
  }, [selectedOption]);

  useEffect(() => {
    if (!selectedOption) {
      setAvailability([]);
      return;
    }

    fetchJson(`/api/reservations/availability?optionId=${selectedOption.optionId}&startDate=${dateOptions[0].value}&days=${DATE_PAGE_SIZE}`)
      .then(setAvailability)
      .catch(showError);
  }, [selectedOption, dateOptions]);

  const completedStep = useMemo(() => {
    if (selectedCenter) {
      return 3;
    }
    if (selectedOption) {
      return 2;
    }
    if (selectedCar) {
      return 1;
    }
    return 0;
  }, [selectedCar, selectedOption, selectedCenter]);

  const enrichedDateOptions = useMemo(() => {
    return dateOptions.map((date) => {
      const serverDate = availability.find((item) => item.date === date.value);
      if (!serverDate) {
        return date;
      }
      return {
        ...date,
        available: serverDate.available,
        reason: serverDate.reason,
        slot: serverDate.available ? date.slot : serverDate.reason,
      };
    });
  }, [availability, dateOptions]);
  const reservationCompleted = reservationFeedback?.type === 'success';

  async function fetchJson(path, options = {}) {
    const response = await fetch(`${API_BASE_URL}${path}`, {
      headers: {'Content-Type': 'application/json'},
      ...options,
    });

    const body = await response.json();
    if (!response.ok) {
      throw new Error(body.message || '서버 요청에 실패했습니다.');
    }
    return body;
  }

  function showError(error) {
    if (viewMode === 'admin') {
      setAdminFeedback({type: 'error', message: error.message});
      return;
    }
    setReservationFeedback({type: 'error', message: error.message});
  }

  function chooseCar(car) {
    setSelectedCar(car);
    setReservationFeedback(null);
    setActiveStep(1);
  }

  function chooseOption(option) {
    setSelectedOption(option);
    setReservationFeedback(null);
    setActiveStep(2);
  }

  function chooseCenter(center) {
    setSelectedCenter(center);
    setReservationFeedback(null);
    setActiveStep(3);
  }

  function openStep(stepIndex) {
    if (stepIndex === 0 || stepIndex <= completedStep) {
      setActiveStep(stepIndex);
    }
  }

  async function submitReservation() {
    const selectedDate = enrichedDateOptions.find((date) => date.value === reservationDate);
    if (selectedDate && selectedDate.available === false) {
      setReservationFeedback({type: 'error', message: `${reservationDate}은 ${selectedDate.reason} 상태라 예약할 수 없습니다.`});
      return;
    }
    if (!selectedOption || !selectedCenter || !reservationDate) {
      setReservationFeedback({type: 'error', message: '트림, 전시장, 시승일을 모두 선택해 주세요.'});
      return;
    }

    setSubmitting(true);
    setReservationFeedback({type: 'info', message: '예약 정보를 서버로 전송하고 있습니다.'});
    try {
      const result = await fetchJson('/api/reservations', {
        method: 'POST',
        body: JSON.stringify({
          centerId: selectedCenter.centerId,
          optionId: selectedOption.optionId,
          reservationDate,
        }),
      });
      setReservationFeedback({type: 'success', message: result.message || '시승 신청이 완료되었습니다.'});
    } catch (error) {
      showError(error);
    } finally {
      setSubmitting(false);
    }
  }

  async function loginAdmin() {
    setAdminLoading(true);
    setAdminFeedback({type: 'info', message: '관리자 정보를 확인하고 있습니다.'});
    try {
      const result = await fetchJson('/api/admin/login', {
        method: 'POST',
        body: JSON.stringify({username: adminUsername, password: adminPassword}),
      });
      setAdminLoggedIn(true);
      setAdminFeedback({type: 'success', message: result.message});
      await loadAdminDashboard();
    } catch (error) {
      setAdminLoggedIn(false);
      setAdminFeedback({type: 'error', message: error.message});
    } finally {
      setAdminLoading(false);
    }
  }

  async function loadAdminDashboard() {
    setAdminLoading(true);
    try {
      const [summary, reservations, products, brands, models] = await Promise.all([
        fetchJson('/api/admin/summary'),
        fetchJson('/api/admin/reservations'),
        fetchJson('/api/admin/products'),
        fetchJson('/api/admin/brands'),
        fetchJson('/api/admin/models'),
      ]);
      setAdminSummary(summary);
      setAdminReservations(reservations);
      setAdminProducts(products);
      setAdminBrands(brands);
      setAdminModels(models);
      setModelBrandId((current) => current ?? brands[0]?.selectionId ?? brands[0]?.id ?? null);
      setOptionCarId((current) => current ?? models[0]?.selectionId ?? models[0]?.id ?? null);
    } catch (error) {
      setAdminFeedback({type: 'error', message: error.message});
    } finally {
      setAdminLoading(false);
    }
  }

  async function createBrand() {
    if (!brandName.trim()) {
      setAdminFeedback({type: 'error', message: '브랜드명을 입력해 주세요.'});
      return;
    }
    await submitAdminCreate('/api/admin/brands', {name: brandName.trim()}, () => setBrandName(''));
  }

  async function createModel() {
    if (!modelBrandId || !modelName.trim()) {
      setAdminFeedback({type: 'error', message: '브랜드와 모델명을 선택해 주세요.'});
      return;
    }
    await submitAdminCreate('/api/admin/models', {brandId: modelBrandId, name: modelName.trim()}, () => setModelName(''));
  }

  async function createVehicleOption() {
    if (!optionCarId || !optionGrade.trim() || !optionColor.trim() || !optionPrice.trim()) {
      setAdminFeedback({type: 'error', message: '모델, 트림, 색상, 가격을 입력해 주세요.'});
      return;
    }
    await submitAdminCreate('/api/admin/vehicle-options', {
      carId: optionCarId,
      color: optionColor.trim(),
      cc: parseNumber(optionCc),
      km: parseNumber(optionKm),
      price: parseNumber(optionPrice),
      grade: optionGrade.trim(),
    }, () => {
      setOptionGrade('');
      setOptionColor('');
      setOptionCc('');
      setOptionKm('');
      setOptionPrice('');
    });
  }

  async function submitAdminCreate(path, payload, resetForm) {
    setAdminLoading(true);
    setAdminFeedback({type: 'info', message: '운영 데이터를 저장하고 있습니다.'});
    try {
      const result = await fetchJson(path, {
        method: 'POST',
        body: JSON.stringify(payload),
      });
      resetForm();
      setAdminFeedback({type: 'success', message: result.message});
      await loadAdminDashboard();
    } catch (error) {
      setAdminFeedback({type: 'error', message: error.message});
    } finally {
      setAdminLoading(false);
    }
  }

  async function updateReservationState(reservationId, state) {
    setAdminFeedback({type: 'info', message: '예약 상태를 업데이트하고 있습니다.'});
    try {
      const result = await fetchJson(`/api/admin/reservations/${reservationId}`, {
        method: 'PATCH',
        body: JSON.stringify({state}),
      });
      setAdminFeedback({type: 'success', message: result.message});
      await loadAdminDashboard();
    } catch (error) {
      setAdminFeedback({type: 'error', message: error.message});
    }
  }

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle="dark-content" />
      <ScrollView contentContainerStyle={styles.page}>
        <View style={styles.heroWrap}>
          <View style={styles.heroCopy}>
            <Text style={styles.eyebrow}>MOTIONVOLT KOREA</Text>
            <Text style={styles.title}>시승 예약, 한 단계씩 정확하게.</Text>
            <Text style={styles.subtitle}>차량과 전시장, 날짜를 앱 안에서 고르고 바로 신청하세요.</Text>
          </View>
          <Image source={HERO_IMAGE} style={styles.heroImage} resizeMode="contain" />
        </View>

        <View style={styles.contentWrap}>
          <ModeSwitch viewMode={viewMode} onChange={setViewMode} />
          {viewMode === 'booking' && <ResourceShowcase isWide={isWide} />}

          {viewMode === 'admin' ? (
            <AdminDashboard
              isWide={isWide}
              loggedIn={adminLoggedIn}
              username={adminUsername}
              password={adminPassword}
              summary={adminSummary}
              reservations={adminReservations}
              products={adminProducts}
              brands={adminBrands}
              models={adminModels}
              brandName={brandName}
              modelBrandId={modelBrandId}
              modelName={modelName}
              optionCarId={optionCarId}
              optionGrade={optionGrade}
              optionColor={optionColor}
              optionCc={optionCc}
              optionKm={optionKm}
              optionPrice={optionPrice}
              loading={adminLoading}
              feedback={adminFeedback}
              onUsernameChange={setAdminUsername}
              onPasswordChange={setAdminPassword}
              onBrandNameChange={setBrandName}
              onModelBrandChange={setModelBrandId}
              onModelNameChange={setModelName}
              onOptionCarChange={setOptionCarId}
              onOptionGradeChange={setOptionGrade}
              onOptionColorChange={setOptionColor}
              onOptionCcChange={setOptionCc}
              onOptionKmChange={setOptionKm}
              onOptionPriceChange={setOptionPrice}
              onLogin={loginAdmin}
              onRefresh={loadAdminDashboard}
              onUpdateState={updateReservationState}
              onCreateBrand={createBrand}
              onCreateModel={createModel}
              onCreateVehicleOption={createVehicleOption}
            />
          ) : loading ? (
            <LoadingState />
          ) : (
            <>
            <ProgressSteps activeStep={activeStep} completedStep={completedStep} onStepPress={openStep} />
            <View style={[styles.workspace, isWide && styles.workspaceWide]}>
              <View style={styles.stagePanel}>
                {activeStep === 0 && (
                  <Stage title="모델 선택" subtitle="대표 라인업 중 시승할 모델을 선택하세요.">
                    <View style={[styles.cardGrid, isWide && styles.cardGridWide]}>
                      {cars.map((car) => (
                        <ModelCard
                          key={car.carId}
                          car={car}
                          selected={selectedCar?.carId === car.carId}
                          wide={isWide}
                          onPress={() => chooseCar(car)}
                        />
                      ))}
                    </View>
                  </Stage>
                )}

                {activeStep === 1 && (
                  <Stage
                    title="트림 선택"
                    subtitle={selectedCar ? `${selectedCar.modelName}의 옵션을 비교하세요.` : '모델을 먼저 선택해 주세요.'}>
                    {!selectedCar ? (
                      <EmptyState text="모델 선택 단계로 돌아가 주세요." />
                    ) : optionLoading ? (
                      <LoadingState compact />
                    ) : (
                      <View style={[styles.cardGrid, isWide && styles.cardGridWide]}>
                        {options.map((option) => (
                          <OptionCard
                            key={option.optionId}
                            option={option}
                            selected={selectedOption?.optionId === option.optionId}
                            onPress={() => chooseOption(option)}
                          />
                        ))}
                      </View>
                    )}
                  </Stage>
                )}

                {activeStep === 2 && (
                  <Stage title="전시장 선택" subtitle="상담과 시승을 진행할 지점을 선택하세요.">
                    {!selectedOption ? (
                      <EmptyState text="트림을 먼저 선택해 주세요." />
                    ) : centerLoading ? (
                      <LoadingState compact />
                    ) : (
                      <View style={[styles.cardGrid, isWide && styles.cardGridWide]}>
                        {centers.map((center) => (
                          <CenterCard
                            key={center.centerId}
                            center={center}
                            selected={selectedCenter?.centerId === center.centerId}
                            onPress={() => chooseCenter(center)}
                          />
                        ))}
                      </View>
                    )}
                  </Stage>
                )}

                {activeStep === 3 && (
                  <Stage title="신청 정보 확인" subtitle="예약일을 선택하고 신청을 완료하세요.">
                    <DatePickerStrip
                      dateOptions={enrichedDateOptions}
                      datePage={datePage}
                      selectedDate={reservationDate}
                      onPrevious={() => setDatePage((currentPage) => Math.max(0, currentPage - 1))}
                      onNext={() => setDatePage((currentPage) => currentPage + 1)}
                      onSelect={(date) => {
                        setReservationDate(date);
                        setReservationFeedback(null);
                      }}
                    />

                    <TextInput
                      value={reservationDate}
                      onChangeText={(date) => {
                        setReservationDate(date);
                        setReservationFeedback(null);
                      }}
                      placeholder={dateOptions[0].value}
                      style={styles.dateInput}
                      autoCapitalize="none"
                      autoCorrect={false}
                      returnKeyType="done"
                    />

                    <ReservationSummary
                      selectedCar={selectedCar}
                      selectedOption={selectedOption}
                      selectedCenter={selectedCenter}
                      reservationDate={reservationDate}
                    />

                    {reservationFeedback && (
                      <StatusBanner type={reservationFeedback.type} message={reservationFeedback.message} />
                    )}

                    <Pressable
                      onPress={submitReservation}
                      disabled={submitting || reservationCompleted || !selectedOption || !selectedCenter}
                      style={({pressed}) => [
                        styles.primaryButton,
                        pressed && styles.pressedButton,
                        (submitting || reservationCompleted || !selectedOption || !selectedCenter) && styles.disabledButton,
                      ]}>
                      <Text style={styles.primaryButtonText}>
                        {reservationCompleted ? '예약 완료됨' : submitting ? '신청 중' : '시승 신청하기'}
                      </Text>
                    </Pressable>
                  </Stage>
                )}
              </View>

              <View style={styles.summaryPanel}>
                <Text style={styles.summaryLabel}>예약 요약</Text>
                <SummaryLine label="모델" value={selectedCar?.modelName || '선택 전'} />
                <SummaryLine label="트림" value={selectedOption?.grade || '선택 전'} />
                <SummaryLine label="전시장" value={selectedCenter?.centerName || '선택 전'} />
                <SummaryLine label="시승일" value={reservationDate} />
              </View>
            </View>
            </>
          )}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

function ModeSwitch({viewMode, onChange}) {
  return (
    <View style={styles.modeSwitch}>
      <Pressable
        onPress={() => onChange('booking')}
        style={[styles.modeButton, viewMode === 'booking' && styles.modeButtonActive]}>
        <Text style={[styles.modeButtonText, viewMode === 'booking' && styles.modeButtonTextActive]}>시승 예약</Text>
      </Pressable>
      <Pressable
        onPress={() => onChange('admin')}
        style={[styles.modeButton, viewMode === 'admin' && styles.modeButtonActive]}>
        <Text style={[styles.modeButtonText, viewMode === 'admin' && styles.modeButtonTextActive]}>관리자</Text>
      </Pressable>
    </View>
  );
}

function ResourceShowcase({isWide}) {
  return (
    <View style={[styles.showcaseGrid, isWide && styles.showcaseGridWide]}>
      {SHOWCASE_IMAGES.map((image, index) => (
        <View key={index} style={styles.showcaseCard}>
          <Image source={image} style={styles.showcaseImage} resizeMode="cover" />
          <View style={styles.showcaseCopy}>
            <Text style={styles.showcaseTitle}>
              {index === 0 ? '고객 예약 흐름' : index === 1 ? '전시장 운영 관리' : '서비스 경험'}
            </Text>
            <Text style={styles.showcaseText}>
              {index === 0 ? '차량 선택부터 신청까지 한 번에 진행합니다.' : index === 1 ? '예약 가능 여부를 바로 조정합니다.' : '기존 리소스를 활용한 통합 화면입니다.'}
            </Text>
          </View>
        </View>
      ))}
    </View>
  );
}

function AdminDashboard({
  isWide,
  loggedIn,
  username,
  password,
  summary,
  reservations,
  products,
  brands,
  models,
  brandName,
  modelBrandId,
  modelName,
  optionCarId,
  optionGrade,
  optionColor,
  optionCc,
  optionKm,
  optionPrice,
  loading,
  feedback,
  onUsernameChange,
  onPasswordChange,
  onBrandNameChange,
  onModelBrandChange,
  onModelNameChange,
  onOptionCarChange,
  onOptionGradeChange,
  onOptionColorChange,
  onOptionCcChange,
  onOptionKmChange,
  onOptionPriceChange,
  onLogin,
  onRefresh,
  onUpdateState,
  onCreateBrand,
  onCreateModel,
  onCreateVehicleOption,
}) {
  if (!loggedIn) {
    return (
      <View style={styles.adminLoginPanel}>
        <Image source={ADMIN_IMAGE} style={styles.adminLogo} resizeMode="contain" />
        <Text style={styles.stageTitle}>관리자 대시보드</Text>
        <Text style={styles.stageSubtitle}>기본 샘플 계정은 admin / admin123 입니다.</Text>
        <TextInput value={username} onChangeText={onUsernameChange} style={styles.dateInput} placeholder="아이디" />
        <TextInput
          value={password}
          onChangeText={onPasswordChange}
          style={styles.dateInput}
          placeholder="비밀번호"
          secureTextEntry
        />
        {feedback && <StatusBanner type={feedback.type} message={feedback.message} />}
        <Pressable onPress={onLogin} disabled={loading} style={[styles.primaryButton, loading && styles.disabledButton]}>
          <Text style={styles.primaryButtonText}>{loading ? '확인 중' : '관리자 접속'}</Text>
        </Pressable>
      </View>
    );
  }

  return (
    <View style={styles.adminWorkspace}>
      <View style={styles.adminHeader}>
        <View>
          <Text style={styles.stageTitle}>예약 운영 대시보드</Text>
          <Text style={styles.stageSubtitle}>시승 예약, 마감 상태, 차량 옵션을 한 곳에서 확인합니다.</Text>
        </View>
        <Pressable onPress={onRefresh} style={styles.dateNavButton}>
          <Text style={styles.dateNavText}>{loading ? '동기화 중' : '새로고침'}</Text>
        </Pressable>
      </View>

      {feedback && <StatusBanner type={feedback.type} message={feedback.message} />}

      <View style={[styles.metricGrid, isWide && styles.metricGridWide]}>
        <MetricCard label="전체 예약" value={formatCount(summary?.reservationCount ?? 0)} />
        <MetricCard label="예약 확정" value={formatCount(summary?.reservedCount ?? 0)} />
        <MetricCard label="예약 불가" value={formatCount(summary?.failedCount ?? 0)} />
        <MetricCard label="운영 트림" value={formatCount(summary?.productCount ?? 0)} />
      </View>

      <AdminCreatePanel
        isWide={isWide}
        brands={brands}
        models={models}
        brandName={brandName}
        modelBrandId={modelBrandId}
        modelName={modelName}
        optionCarId={optionCarId}
        optionGrade={optionGrade}
        optionColor={optionColor}
        optionCc={optionCc}
        optionKm={optionKm}
        optionPrice={optionPrice}
        loading={loading}
        onBrandNameChange={onBrandNameChange}
        onModelBrandChange={onModelBrandChange}
        onModelNameChange={onModelNameChange}
        onOptionCarChange={onOptionCarChange}
        onOptionGradeChange={onOptionGradeChange}
        onOptionColorChange={onOptionColorChange}
        onOptionCcChange={onOptionCcChange}
        onOptionKmChange={onOptionKmChange}
        onOptionPriceChange={onOptionPriceChange}
        onCreateBrand={onCreateBrand}
        onCreateModel={onCreateModel}
        onCreateVehicleOption={onCreateVehicleOption}
      />

      <View style={[styles.adminColumns, isWide && styles.adminColumnsWide]}>
        <View style={styles.adminColumn}>
          <Text style={styles.summaryLabel}>예약 목록</Text>
          {reservations.map((reservation) => (
            <AdminReservationCard
              key={reservation.reservationId}
              reservation={reservation}
              onUpdateState={onUpdateState}
            />
          ))}
        </View>
        <View style={styles.adminColumn}>
          <Text style={styles.summaryLabel}>차량 옵션</Text>
          {products.map((product) => (
            <ProductCard key={product.optionId} product={product} />
          ))}
        </View>
      </View>
    </View>
  );
}

function MetricCard({label, value}) {
  return (
    <View style={styles.metricCard}>
      <Text style={styles.metricValue}>{value}</Text>
      <Text style={styles.metricLabel}>{label}</Text>
    </View>
  );
}

function AdminCreatePanel({
  isWide,
  brands = [],
  models = [],
  brandName,
  modelBrandId,
  modelName,
  optionCarId,
  optionGrade,
  optionColor,
  optionCc,
  optionKm,
  optionPrice,
  loading,
  onBrandNameChange,
  onModelBrandChange,
  onModelNameChange,
  onOptionCarChange,
  onOptionGradeChange,
  onOptionColorChange,
  onOptionCcChange,
  onOptionKmChange,
  onOptionPriceChange,
  onCreateBrand,
  onCreateModel,
  onCreateVehicleOption,
}) {
  return (
    <View style={styles.adminCreatePanel}>
      <View style={styles.sectionHeader}>
        <Text style={styles.summaryLabel}>차량 데이터 생성</Text>
        <Text style={styles.stageSubtitle}>브랜드, 모델, 트림 옵션을 관리자 화면에서 분리해서 등록합니다.</Text>
      </View>

      <View style={[styles.adminCreateGrid, isWide && styles.adminCreateGridWide]}>
        <View style={styles.createBox}>
          <Text style={styles.createTitle}>브랜드 생성</Text>
          <View style={styles.createBody}>
            <AdminField label="브랜드명">
              <TextInput
                value={brandName}
                onChangeText={onBrandNameChange}
                style={styles.adminTextInput}
                placeholder="예: MotionVolt"
                returnKeyType="done"
              />
            </AdminField>
          </View>
          <Pressable onPress={onCreateBrand} disabled={loading} style={[styles.secondaryButton, loading && styles.disabledOutlineButton]}>
            <Text style={styles.secondaryButtonText}>브랜드 저장</Text>
          </Pressable>
        </View>

        <View style={styles.createBox}>
          <Text style={styles.createTitle}>모델 생성</Text>
          <View style={styles.createBody}>
            <ChoiceList
              items={brands}
              selectedId={modelBrandId}
              onSelect={onModelBrandChange}
              emptyText="브랜드를 먼저 생성하세요."
            />
            <AdminField label="모델명">
              <TextInput
                value={modelName}
                onChangeText={onModelNameChange}
                style={styles.adminTextInput}
                placeholder="예: EV9 GT Line"
                returnKeyType="done"
              />
            </AdminField>
          </View>
          <Pressable onPress={onCreateModel} disabled={loading || brands.length === 0} style={[styles.secondaryButton, (loading || brands.length === 0) && styles.disabledOutlineButton]}>
            <Text style={styles.secondaryButtonText}>모델 저장</Text>
          </Pressable>
        </View>

        <View style={styles.createBox}>
          <Text style={styles.createTitle}>트림 옵션 생성</Text>
          <View style={styles.createBody}>
            <ChoiceList
              items={models}
              selectedId={optionCarId}
              onSelect={onOptionCarChange}
              emptyText="모델을 먼저 생성하세요."
            />
            <View style={styles.formRow}>
              <AdminField label="트림명" style={styles.formInput}>
                <TextInput value={optionGrade} onChangeText={onOptionGradeChange} style={styles.adminTextInput} placeholder="예: Signature" />
              </AdminField>
              <AdminField label="색상" style={styles.formInput}>
                <TextInput value={optionColor} onChangeText={onOptionColorChange} style={styles.adminTextInput} placeholder="예: Uyuni White" />
              </AdminField>
            </View>
            <View style={styles.formRow}>
              <AdminField label="배기량" unit="cc" style={styles.formInput}>
                <TextInput value={optionCc} onChangeText={onOptionCcChange} style={[styles.adminTextInput, styles.unitInput]} placeholder="0" keyboardType="number-pad" />
              </AdminField>
              <AdminField label="연비" unit="km/L" style={styles.formInput}>
                <TextInput value={optionKm} onChangeText={onOptionKmChange} style={[styles.adminTextInput, styles.unitInput]} placeholder="0" keyboardType="number-pad" />
              </AdminField>
              <AdminField label="가격" unit="만원" style={styles.formInput}>
                <TextInput value={optionPrice} onChangeText={onOptionPriceChange} style={[styles.adminTextInput, styles.unitInput]} placeholder="8900" keyboardType="number-pad" />
              </AdminField>
            </View>
          </View>
          <Pressable onPress={onCreateVehicleOption} disabled={loading || models.length === 0} style={[styles.secondaryButton, (loading || models.length === 0) && styles.disabledOutlineButton]}>
            <Text style={styles.secondaryButtonText}>트림 저장</Text>
          </Pressable>
        </View>
      </View>
    </View>
  );
}

function AdminField({label, unit, style, children}) {
  return (
    <View style={[styles.fieldGroup, style]}>
      <Text style={styles.fieldLabel}>{label}</Text>
      <View style={unit ? styles.unitInputWrap : null}>
        {children}
        {unit && <Text style={styles.unitText}>{unit}</Text>}
      </View>
    </View>
  );
}

function ChoiceList({items = [], selectedId, onSelect, emptyText}) {
  if (items.length === 0) {
    return <Text style={styles.choiceEmpty}>{emptyText}</Text>;
  }

  return (
    <View style={styles.choiceGrid}>
      {items.map((item) => {
        const itemId = item.id ?? item.selectionId;
        const itemName = item.name ?? item.selectionName;
        const selected = selectedId === itemId;
        return (
          <Pressable
            key={itemId}
            onPress={() => onSelect(itemId)}
            style={[styles.choiceButton, selected && styles.choiceButtonActive]}>
            <Text style={[styles.choiceButtonText, selected && styles.choiceButtonTextActive]}>{itemName}</Text>
          </Pressable>
        );
      })}
    </View>
  );
}

function AdminReservationCard({reservation, onUpdateState}) {
  const reserved = reservation.state === 'RESERVED';
  return (
    <View style={styles.adminItemCard}>
      <View style={styles.adminItemHeader}>
        <View style={styles.adminItemTitle}>
          <Text style={styles.cardTitle}>{reservation.modelName}</Text>
          <Text style={styles.cardMeta}>{reservation.customerName} · {reservation.reservationDate}</Text>
        </View>
        <View style={[styles.statusPill, reserved ? styles.statusPillReserved : styles.statusPillFailed]}>
          <Text style={[styles.statusPillText, reserved ? styles.statusPillTextReserved : styles.statusPillTextFailed]}>
            {reserved ? '예약 가능' : '예약 불가'}
          </Text>
        </View>
      </View>
      <Text style={styles.cardText}>
        {reservation.grade} · {reservation.color} · {formatCc(reservation.cc)} · {formatEfficiency(reservation.km)}
      </Text>
      <View style={styles.adminActionRow}>
        <Pressable
          onPress={() => onUpdateState(reservation.reservationId, 'RESERVED')}
          style={[styles.smallActionButton, reserved && styles.smallActionButtonActive]}>
          <Text style={[styles.smallActionText, reserved && styles.smallActionTextActive]}>예약 가능</Text>
        </Pressable>
        <Pressable
          onPress={() => onUpdateState(reservation.reservationId, 'FAILED')}
          style={[styles.smallActionButton, !reserved && styles.smallActionButtonDanger]}>
          <Text style={[styles.smallActionText, !reserved && styles.smallActionTextDanger]}>예약 불가</Text>
        </Pressable>
      </View>
    </View>
  );
}

function ProductCard({product}) {
  return (
    <View style={styles.adminItemCard}>
      <Text style={styles.cardTitle}>{product.modelName}</Text>
      <Text style={styles.cardMeta}>{product.brandName} · {product.grade}</Text>
      <View style={styles.specRow}>
        <Spec label="색상" value={product.color} />
        <Spec label="배기량" value={formatCc(product.cc)} />
        <Spec label="연비" value={formatEfficiency(product.km)} />
        <Spec label="가격" value={formatKoreanPrice(product.price)} />
      </View>
    </View>
  );
}

function DatePickerStrip({dateOptions, datePage, selectedDate, onPrevious, onNext, onSelect}) {
  return (
    <View style={styles.datePicker}>
      <View style={styles.datePickerHeader}>
        <View>
          <Text style={styles.datePickerTitle}>가능한 날짜</Text>
          <Text style={styles.datePickerSubtitle}>오늘 이후 날짜가 자동으로 생성됩니다.</Text>
        </View>
        <View style={styles.dateNav}>
          <Pressable
            disabled={datePage === 0}
            onPress={onPrevious}
            style={[styles.dateNavButton, datePage === 0 && styles.dateNavButtonDisabled]}>
            <Text style={[styles.dateNavText, datePage === 0 && styles.dateNavTextDisabled]}>이전</Text>
          </Pressable>
          <Pressable onPress={onNext} style={styles.dateNavButton}>
            <Text style={styles.dateNavText}>다음</Text>
          </Pressable>
        </View>
      </View>
      <View style={styles.dateGrid}>
        {dateOptions.map((date) => {
          const selected = selectedDate === date.value;
          const disabled = date.available === false;
          return (
            <Pressable
              key={date.value}
              disabled={disabled}
              onPress={() => onSelect(date.value)}
              style={[styles.dateChip, selected && styles.dateChipActive, disabled && styles.dateChipDisabled]}>
              <Text style={[styles.dateChipWeekday, selected && styles.dateChipTextActive, disabled && styles.dateChipTextDisabled]}>
                {date.weekday}
              </Text>
              <Text style={[styles.dateChipText, selected && styles.dateChipTextActive, disabled && styles.dateChipTextDisabled]}>
                {date.label}
              </Text>
              <Text style={[styles.dateChipSubtext, selected && styles.dateChipTextActive, disabled && styles.dateChipTextDisabled]}>
                {date.slot}
              </Text>
            </Pressable>
          );
        })}
      </View>
    </View>
  );
}

function ProgressSteps({activeStep, completedStep, onStepPress}) {
  return (
    <View style={styles.stepRow}>
      {STEPS.map((step, index) => {
        const enabled = index === 0 || index <= completedStep;
        const active = index === activeStep;
        const done = index < completedStep;

        return (
          <Pressable
            key={step}
            disabled={!enabled}
            onPress={() => onStepPress(index)}
            style={[styles.stepPill, active && styles.stepPillActive, !enabled && styles.stepPillDisabled]}>
            <Text style={[styles.stepNumber, active && styles.stepNumberActive]}>{done ? '✓' : index + 1}</Text>
            <Text style={[styles.stepText, active && styles.stepTextActive]}>{step}</Text>
          </Pressable>
        );
      })}
    </View>
  );
}

function Stage({title, subtitle, children}) {
  return (
    <View>
      <View style={styles.stageHeader}>
        <Text style={styles.stageTitle}>{title}</Text>
        <Text style={styles.stageSubtitle}>{subtitle}</Text>
      </View>
      {children}
    </View>
  );
}

function ModelCard({car, selected, wide, onPress}) {
  return (
    <Pressable onPress={onPress} style={[styles.modelCard, wide && styles.modelCardWide, selected && styles.selectedCard]}>
      <Image source={imageForCar(car.modelName)} style={styles.modelImage} resizeMode="contain" />
      <View style={styles.cardCopy}>
        <Text style={styles.cardMeta}>{car.brandName}</Text>
        <Text style={styles.cardTitle}>{car.modelName}</Text>
        <Text style={styles.cardText}>전시장 상담과 시승 가능</Text>
      </View>
    </Pressable>
  );
}

function OptionCard({option, selected, onPress}) {
  return (
    <Pressable onPress={onPress} style={[styles.infoCard, selected && styles.selectedCard]}>
      <Text style={styles.cardTitle}>{option.grade}</Text>
      <Text style={styles.cardMeta}>{option.color}</Text>
      <View style={styles.specRow}>
        <Spec label="배기량" value={formatCc(option.cc)} />
        <Spec label="연비" value={formatEfficiency(option.km)} />
        <Spec label="가격" value={formatKoreanPrice(option.price)} />
      </View>
    </Pressable>
  );
}

function CenterCard({center, selected, onPress}) {
  return (
    <Pressable onPress={onPress} style={[styles.infoCard, selected && styles.selectedCard]}>
      <Text style={styles.cardTitle}>{center.centerName}</Text>
      <Text style={styles.cardText}>{center.address}</Text>
      <Text style={styles.phone}>{center.phoneNumber}</Text>
    </Pressable>
  );
}

function ReservationSummary({selectedCar, selectedOption, selectedCenter, reservationDate}) {
  return (
    <View style={styles.confirmBox}>
      <SummaryLine label="차량" value={selectedCar?.modelName || '모델 선택 필요'} />
      <SummaryLine label="트림" value={selectedOption?.grade || '트림 선택 필요'} />
      <SummaryLine label="전시장" value={selectedCenter?.centerName || '전시장 선택 필요'} />
      <SummaryLine label="날짜" value={reservationDate} />
    </View>
  );
}

function StatusBanner({type, message}) {
  return (
    <View style={[styles.statusBanner, styles[`statusBanner_${type}`]]}>
      <Text style={[styles.statusBannerText, styles[`statusBannerText_${type}`]]}>{message}</Text>
    </View>
  );
}

function SummaryLine({label, value}) {
  return (
    <View style={styles.summaryLine}>
      <Text style={styles.summaryKey}>{label}</Text>
      <Text style={styles.summaryValue}>{value}</Text>
    </View>
  );
}

function Spec({label, value}) {
  return (
    <View style={styles.specBox}>
      <Text style={styles.specLabel}>{label}</Text>
      <Text style={styles.specValue}>{value}</Text>
    </View>
  );
}

function EmptyState({text}) {
  return (
    <View style={styles.emptyBox}>
      <Text style={styles.emptyText}>{text}</Text>
    </View>
  );
}

function LoadingState({compact = false}) {
  return (
    <View style={[styles.loadingBox, compact && styles.loadingBoxCompact]}>
      <ActivityIndicator size="large" color="#0071E3" />
      <Text style={styles.loadingText}>데이터를 불러오는 중입니다.</Text>
    </View>
  );
}

function imageForCar(modelName) {
  return CAR_IMAGES[modelName] || CAR_IMAGES.LC500h;
}

function buildAvailableDates(page) {
  const firstDate = new Date();
  firstDate.setHours(12, 0, 0, 0);
  firstDate.setDate(firstDate.getDate() + 1 + page * DATE_PAGE_SIZE);

  return Array.from({length: DATE_PAGE_SIZE}, (_, index) => {
    const date = new Date(firstDate);
    date.setDate(firstDate.getDate() + index);

    return {
      value: toIsoDate(date),
      label: `${date.getMonth() + 1}.${date.getDate()}`,
      weekday: new Intl.DateTimeFormat('ko-KR', {weekday: 'short'}).format(date),
      slot: index % 2 === 0 ? '오전 가능' : '오후 가능',
    };
  });
}

function toIsoDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

function formatCount(value) {
  return `${formatNumber(value)}건`;
}

function formatCc(value) {
  return Number(value) === 0 ? '전기' : `${formatNumber(value)}cc`;
}

function formatEfficiency(value) {
  return Number(value) === 0 ? '전비 별도' : `${formatNumber(value)}km/L`;
}

function formatKoreanPrice(value) {
  const amount = Math.round(Number(value));
  if (!Number.isFinite(amount)) {
    return '-';
  }
  if (amount >= 10000) {
    const eok = Math.floor(amount / 10000);
    const man = amount % 10000;
    return man === 0 ? `${formatNumber(eok)}억원` : `${formatNumber(eok)}억 ${formatNumber(man)}만원`;
  }
  return `${formatNumber(amount)}만원`;
}

function formatNumber(value) {
  return new Intl.NumberFormat('ko-KR').format(Number(value) || 0);
}

function parseNumber(value) {
  const parsed = Number(String(value).replace(/,/g, '').trim());
  return Number.isFinite(parsed) ? parsed : 0;
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#F5F5F7',
  },
  page: {
    alignItems: 'center',
    paddingBottom: 42,
  },
  heroWrap: {
    alignItems: 'center',
    backgroundColor: '#FBFBFD',
    paddingHorizontal: 24,
    paddingTop: 54,
    width: '100%',
  },
  heroCopy: {
    alignItems: 'center',
    maxWidth: 720,
  },
  eyebrow: {
    color: '#6E6E73',
    fontSize: 12,
    fontWeight: '800',
    letterSpacing: 0,
  },
  title: {
    color: '#1D1D1F',
    fontSize: 40,
    fontWeight: '900',
    lineHeight: 49,
    marginTop: 10,
    textAlign: 'center',
  },
  subtitle: {
    color: '#6E6E73',
    fontSize: 18,
    lineHeight: 27,
    marginTop: 10,
    textAlign: 'center',
  },
  heroImage: {
    height: 250,
    marginTop: 12,
    maxWidth: 760,
    width: '100%',
  },
  contentWrap: {
    maxWidth: 1180,
    paddingHorizontal: 20,
    width: '100%',
  },
  modeSwitch: {
    alignSelf: 'center',
    backgroundColor: '#EDEDF0',
    borderRadius: 8,
    flexDirection: 'row',
    gap: 4,
    marginTop: 18,
    padding: 4,
  },
  modeButton: {
    borderRadius: 8,
    paddingHorizontal: 20,
    paddingVertical: 10,
  },
  modeButtonActive: {
    backgroundColor: '#FFFFFF',
    shadowColor: '#000000',
    shadowOffset: {width: 0, height: 4},
    shadowOpacity: 0.08,
    shadowRadius: 12,
  },
  modeButtonText: {
    color: '#6E6E73',
    fontSize: 14,
    fontWeight: '900',
  },
  modeButtonTextActive: {
    color: '#1D1D1F',
  },
  showcaseGrid: {
    gap: 12,
    marginTop: 18,
  },
  showcaseGridWide: {
    flexDirection: 'row',
  },
  showcaseCard: {
    backgroundColor: '#FFFFFF',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    flex: 1,
    overflow: 'hidden',
  },
  showcaseImage: {
    height: 160,
    width: '100%',
  },
  showcaseCopy: {
    padding: 14,
  },
  showcaseTitle: {
    color: '#1D1D1F',
    fontSize: 17,
    fontWeight: '900',
  },
  showcaseText: {
    color: '#6E6E73',
    fontSize: 14,
    lineHeight: 20,
    marginTop: 4,
  },
  stepRow: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 22,
  },
  stepPill: {
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    flex: 1,
    minHeight: 58,
    paddingVertical: 10,
  },
  stepPillActive: {
    borderColor: '#0071E3',
    shadowColor: '#0071E3',
    shadowOffset: {width: 0, height: 8},
    shadowOpacity: 0.12,
    shadowRadius: 18,
  },
  stepPillDisabled: {
    opacity: 0.45,
  },
  stepNumber: {
    color: '#86868B',
    fontSize: 12,
    fontWeight: '900',
  },
  stepNumberActive: {
    color: '#0071E3',
  },
  stepText: {
    color: '#1D1D1F',
    fontSize: 13,
    fontWeight: '800',
    marginTop: 3,
  },
  stepTextActive: {
    color: '#0071E3',
  },
  workspace: {
    gap: 16,
    marginTop: 18,
  },
  workspaceWide: {
    alignItems: 'flex-start',
    flexDirection: 'row',
  },
  stagePanel: {
    backgroundColor: '#FFFFFF',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    flex: 1,
    padding: 20,
  },
  stageHeader: {
    marginBottom: 18,
  },
  stageTitle: {
    color: '#1D1D1F',
    fontSize: 28,
    fontWeight: '900',
    lineHeight: 35,
  },
  stageSubtitle: {
    color: '#6E6E73',
    fontSize: 15,
    lineHeight: 23,
    marginTop: 5,
  },
  cardGrid: {
    gap: 12,
  },
  cardGridWide: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  modelCard: {
    backgroundColor: '#F5F5F7',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    overflow: 'hidden',
  },
  modelCardWide: {
    flexBasis: '31.8%',
    minWidth: 240,
  },
  selectedCard: {
    borderColor: '#0071E3',
    borderWidth: 2,
  },
  modelImage: {
    height: 150,
    width: '100%',
  },
  cardCopy: {
    backgroundColor: '#FFFFFF',
    padding: 15,
  },
  infoCard: {
    backgroundColor: '#FFFFFF',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    flexGrow: 1,
    flexShrink: 1,
    flexBasis: 260,
    padding: 16,
  },
  cardMeta: {
    color: '#86868B',
    fontSize: 13,
    lineHeight: 19,
  },
  cardTitle: {
    color: '#1D1D1F',
    fontSize: 20,
    fontWeight: '900',
    lineHeight: 27,
    marginBottom: 4,
  },
  cardText: {
    color: '#6E6E73',
    fontSize: 14,
    lineHeight: 21,
  },
  specRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    marginTop: 14,
  },
  specBox: {
    backgroundColor: '#F5F5F7',
    borderRadius: 8,
    flex: 1,
    minWidth: 88,
    padding: 10,
  },
  specLabel: {
    color: '#86868B',
    fontSize: 12,
  },
  specValue: {
    color: '#1D1D1F',
    fontSize: 14,
    fontWeight: '800',
    marginTop: 3,
  },
  phone: {
    color: '#0071E3',
    fontSize: 15,
    fontWeight: '800',
    marginTop: 8,
  },
  dateGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    marginBottom: 12,
  },
  datePicker: {
    backgroundColor: '#F5F5F7',
    borderRadius: 8,
    marginBottom: 14,
    padding: 14,
  },
  datePickerHeader: {
    alignItems: 'center',
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 12,
  },
  datePickerTitle: {
    color: '#1D1D1F',
    fontSize: 17,
    fontWeight: '900',
  },
  datePickerSubtitle: {
    color: '#6E6E73',
    fontSize: 13,
    lineHeight: 19,
    marginTop: 2,
  },
  dateNav: {
    flexDirection: 'row',
    gap: 6,
  },
  dateNavButton: {
    backgroundColor: '#FFFFFF',
    borderColor: '#D2D2D7',
    borderRadius: 8,
    borderWidth: 1,
    paddingHorizontal: 12,
    paddingVertical: 8,
  },
  dateNavButtonDisabled: {
    opacity: 0.42,
  },
  dateNavText: {
    color: '#0071E3',
    fontSize: 13,
    fontWeight: '900',
  },
  dateNavTextDisabled: {
    color: '#86868B',
  },
  dateChip: {
    backgroundColor: '#FFFFFF',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    minWidth: 92,
    paddingHorizontal: 13,
    paddingVertical: 12,
  },
  dateChipActive: {
    backgroundColor: '#0071E3',
    borderColor: '#0071E3',
    shadowColor: '#0071E3',
    shadowOffset: {width: 0, height: 8},
    shadowOpacity: 0.18,
    shadowRadius: 16,
  },
  dateChipDisabled: {
    backgroundColor: '#F2F2F4',
    borderColor: '#E1E1E5',
    opacity: 0.72,
  },
  dateChipWeekday: {
    color: '#86868B',
    fontSize: 12,
    fontWeight: '900',
  },
  dateChipText: {
    color: '#1D1D1F',
    fontSize: 18,
    fontWeight: '900',
    marginTop: 4,
  },
  dateChipSubtext: {
    color: '#6E6E73',
    fontSize: 12,
    fontWeight: '800',
    marginTop: 4,
  },
  dateChipTextActive: {
    color: '#FFFFFF',
  },
  dateChipTextDisabled: {
    color: '#A1A1A6',
  },
  dateInput: {
    borderColor: '#D2D2D7',
    borderRadius: 8,
    borderWidth: 1,
    color: '#1D1D1F',
    fontSize: 17,
    marginBottom: 14,
    paddingHorizontal: 14,
    paddingVertical: 13,
  },
  adminTextInput: {
    borderColor: '#D2D2D7',
    borderRadius: 8,
    borderWidth: 1,
    color: '#1D1D1F',
    fontSize: 15,
    minHeight: 46,
    paddingHorizontal: 12,
    paddingVertical: 11,
  },
  unitInput: {
    paddingRight: 54,
  },
  confirmBox: {
    backgroundColor: '#F5F5F7',
    borderRadius: 8,
    marginBottom: 14,
    padding: 14,
  },
  statusBanner: {
    borderRadius: 8,
    marginBottom: 14,
    paddingHorizontal: 14,
    paddingVertical: 13,
  },
  statusBanner_success: {
    backgroundColor: '#EAF7EE',
    borderColor: '#2E8540',
    borderWidth: 1,
  },
  statusBanner_error: {
    backgroundColor: '#FFF2F2',
    borderColor: '#D93025',
    borderWidth: 1,
  },
  statusBanner_info: {
    backgroundColor: '#EFF6FF',
    borderColor: '#0071E3',
    borderWidth: 1,
  },
  statusBannerText: {
    fontSize: 15,
    fontWeight: '900',
    lineHeight: 21,
  },
  statusBannerText_success: {
    color: '#1F6F35',
  },
  statusBannerText_error: {
    color: '#B3261E',
  },
  statusBannerText_info: {
    color: '#0057B8',
  },
  primaryButton: {
    alignItems: 'center',
    backgroundColor: '#1D1D1F',
    borderRadius: 8,
    paddingVertical: 15,
  },
  pressedButton: {
    opacity: 0.82,
  },
  disabledButton: {
    backgroundColor: '#A1A1A6',
  },
  primaryButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '900',
  },
  adminLoginPanel: {
    alignSelf: 'center',
    backgroundColor: '#FFFFFF',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    marginTop: 20,
    maxWidth: 480,
    padding: 24,
    width: '100%',
  },
  adminLogo: {
    height: 54,
    marginBottom: 18,
    width: 160,
  },
  adminWorkspace: {
    backgroundColor: '#FFFFFF',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    marginTop: 20,
    padding: 20,
  },
  adminHeader: {
    alignItems: 'flex-start',
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 16,
    marginBottom: 18,
  },
  metricGrid: {
    gap: 10,
    marginBottom: 18,
  },
  metricGridWide: {
    flexDirection: 'row',
  },
  metricCard: {
    backgroundColor: '#F5F5F7',
    borderRadius: 8,
    flex: 1,
    padding: 16,
  },
  metricValue: {
    color: '#1D1D1F',
    fontSize: 30,
    fontWeight: '900',
  },
  metricLabel: {
    color: '#6E6E73',
    fontSize: 13,
    fontWeight: '800',
    marginTop: 4,
  },
  adminColumns: {
    gap: 14,
  },
  adminColumnsWide: {
    flexDirection: 'row',
  },
  adminCreatePanel: {
    backgroundColor: '#FBFBFD',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    marginBottom: 18,
    padding: 16,
  },
  sectionHeader: {
    marginBottom: 12,
  },
  adminCreateGrid: {
    gap: 12,
  },
  adminCreateGridWide: {
    flexDirection: 'row',
  },
  createBox: {
    backgroundColor: '#FFFFFF',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    flex: 1,
    gap: 12,
    justifyContent: 'space-between',
    minHeight: 168,
    padding: 14,
  },
  createBody: {
    gap: 10,
  },
  createTitle: {
    color: '#1D1D1F',
    fontSize: 16,
    fontWeight: '900',
    marginBottom: 10,
  },
  choiceGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  choiceButton: {
    backgroundColor: '#F5F5F7',
    borderColor: '#D2D2D7',
    borderRadius: 8,
    borderWidth: 1,
    paddingHorizontal: 10,
    paddingVertical: 8,
  },
  choiceButtonActive: {
    backgroundColor: '#1D1D1F',
    borderColor: '#1D1D1F',
  },
  choiceButtonText: {
    color: '#1D1D1F',
    fontSize: 12,
    fontWeight: '900',
  },
  choiceButtonTextActive: {
    color: '#FFFFFF',
  },
  choiceEmpty: {
    color: '#86868B',
    fontSize: 13,
    fontWeight: '800',
    lineHeight: 19,
    marginBottom: 12,
  },
  formRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  formInput: {
    flex: 1,
    minWidth: 132,
  },
  fieldGroup: {
    gap: 6,
  },
  fieldLabel: {
    color: '#6E6E73',
    fontSize: 12,
    fontWeight: '900',
  },
  unitInputWrap: {
    position: 'relative',
  },
  unitText: {
    color: '#86868B',
    fontSize: 12,
    fontWeight: '900',
    position: 'absolute',
    right: 12,
    top: 15,
  },
  secondaryButton: {
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    borderColor: '#1D1D1F',
    borderRadius: 8,
    borderWidth: 1,
    paddingVertical: 12,
  },
  secondaryButtonText: {
    color: '#1D1D1F',
    fontSize: 14,
    fontWeight: '900',
  },
  disabledOutlineButton: {
    opacity: 0.45,
  },
  adminColumn: {
    flex: 1,
    gap: 10,
  },
  adminItemCard: {
    backgroundColor: '#FBFBFD',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    padding: 14,
  },
  adminItemHeader: {
    alignItems: 'flex-start',
    flexDirection: 'row',
    gap: 12,
    justifyContent: 'space-between',
  },
  adminItemTitle: {
    flex: 1,
  },
  statusPill: {
    borderRadius: 8,
    paddingHorizontal: 10,
    paddingVertical: 6,
  },
  statusPillReserved: {
    backgroundColor: '#EAF7EE',
  },
  statusPillFailed: {
    backgroundColor: '#FFF2F2',
  },
  statusPillText: {
    fontSize: 12,
    fontWeight: '900',
  },
  statusPillTextReserved: {
    color: '#1F6F35',
  },
  statusPillTextFailed: {
    color: '#B3261E',
  },
  adminActionRow: {
    flexDirection: 'row',
    gap: 8,
    marginTop: 12,
  },
  smallActionButton: {
    backgroundColor: '#FFFFFF',
    borderColor: '#D2D2D7',
    borderRadius: 8,
    borderWidth: 1,
    paddingHorizontal: 12,
    paddingVertical: 9,
  },
  smallActionButtonActive: {
    backgroundColor: '#0071E3',
    borderColor: '#0071E3',
  },
  smallActionButtonDanger: {
    backgroundColor: '#FFF2F2',
    borderColor: '#D93025',
  },
  smallActionText: {
    color: '#1D1D1F',
    fontSize: 13,
    fontWeight: '900',
  },
  smallActionTextActive: {
    color: '#FFFFFF',
  },
  smallActionTextDanger: {
    color: '#B3261E',
  },
  summaryPanel: {
    backgroundColor: '#FFFFFF',
    borderColor: '#E5E5EA',
    borderRadius: 8,
    borderWidth: 1,
    padding: 18,
    width: 300,
  },
  summaryLabel: {
    color: '#1D1D1F',
    fontSize: 18,
    fontWeight: '900',
    marginBottom: 8,
  },
  summaryLine: {
    borderBottomColor: '#F1F1F3',
    borderBottomWidth: 1,
    paddingVertical: 10,
  },
  summaryKey: {
    color: '#86868B',
    fontSize: 12,
    fontWeight: '800',
  },
  summaryValue: {
    color: '#1D1D1F',
    fontSize: 15,
    fontWeight: '800',
    lineHeight: 22,
    marginTop: 3,
  },
  emptyBox: {
    alignItems: 'center',
    backgroundColor: '#F5F5F7',
    borderRadius: 8,
    padding: 24,
  },
  emptyText: {
    color: '#6E6E73',
    fontSize: 15,
    fontWeight: '700',
  },
  loadingBox: {
    alignItems: 'center',
    paddingTop: 42,
  },
  loadingBoxCompact: {
    paddingBottom: 28,
    paddingTop: 28,
  },
  loadingText: {
    color: '#6E6E73',
    fontSize: 15,
    marginTop: 12,
  },
});
